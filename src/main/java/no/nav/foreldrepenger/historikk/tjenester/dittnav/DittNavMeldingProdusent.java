package no.nav.foreldrepenger.historikk.tjenester.dittnav;

import static no.nav.foreldrepenger.historikk.tjenester.dittnav.DittNavMapper.*;

import java.util.UUID;

import no.nav.brukernotifikasjon.schemas.input.NokkelInput;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaOperations;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import no.nav.foreldrepenger.historikk.domain.Fødselsnummer;
import no.nav.foreldrepenger.historikk.tjenester.innsending.InnsendingHendelse;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Service
public class DittNavMeldingProdusent implements DittNav {

    private static final Logger LOG = LoggerFactory.getLogger(DittNavMeldingProdusent.class);

    private static final String LEGACY_OPPGAVE = "O";
    private static final String OPPGAVE = "C";
    private static final String BESKJED = "B";

    private final DittNavConfig config;
    private final DittNavMeldingsHistorikk lager;
    private final KafkaOperations<NokkelInput, Object> kafkaOperations;

    public DittNavMeldingProdusent(DittNavConfig config,
                                   DittNavMeldingsHistorikk lager,
                                   KafkaOperations<NokkelInput, Object> kafkaOperations) {
        this.config = config;
        this.lager = lager;
        this.kafkaOperations = kafkaOperations;
    }

    @Override
    public void avsluttOppgave(Fødselsnummer fnr, String grupperingsId, String eventId) {
        // Litt slalåm i denne omgang..
        // Hittil har vi lagret og sendt eventer som UUID med prefix B eller O. Etter overgang Aiven krever
        // Brukernotifikasjon ren UUID uten prefix. Unntaket er ved Done-event til eksisterende beholdning.
        var jpaOppgave = lager.hentOppgave(LEGACY_OPPGAVE + eventId, OPPGAVE + eventId);
        if (jpaOppgave != null) {
            var lokalReferanse = jpaOppgave.getReferanseId();
            lager.slett(lokalReferanse);
            var keyEventId = LEGACY_OPPGAVE.equals(lokalReferanse.substring(0,1))
                ? lokalReferanse
                : eventId;
            var key = avsluttNøkkel(fnr, keyEventId, grupperingsId);
            LOG.info("Avslutter oppgave med eventId: {}, grupperingsId: {}", key.getEventId(), grupperingsId);
            send(avslutt(), key, config.getDone());
        } else {
            LOG.info("Ingen oppgave å avslutte i Ditt Nav");
        }
    }

    @Override
    public void opprettBeskjed(InnsendingHendelse h, String tekst) {
        var internReferanse = BESKJED + h.getReferanseId();
        if (!lager.erOpprettet(internReferanse)) {
            NokkelInput key;
            var eventId = UUID.randomUUID().toString();
            if (h.getSaksnummer() != null) {
                key = nøkkel(h.getFnr(), eventId, h.getSaksnummer());
                LOG.info("Oppretter beskjed med rnd eventId: {}, internReferanse: {}, saksnummer: {}, tekst: {}",
                    key.getEventId(), internReferanse, h.getSaksnummer(), tekst);
            } else {
                key = nøkkel(h.getFnr(), eventId, UUID.randomUUID().toString());
                LOG.info("Oppretter beskjed med random eventId: {}, random grupperingsId {} (mangler saksnummer), internReferanse: {}, tekst: {}",
                    key.getEventId(), key.getGrupperingsId(), internReferanse, tekst);
            }
            send(beskjed(tekst, config.uri(), config.getBeskjedVarighet()), key, config.getBeskjed());
            lager.opprettBeskjed(h.getFnr(), key.getGrupperingsId(), h.getReferanseId(), eventId);
        } else {
            LOG.info("Det er allerede opprettet en beskjed {} ", internReferanse);
        }
    }

    @Override
    public void opprettOppgave(InnsendingHendelse h, String tekst) {
        if (!lager.erOpprettet(LEGACY_OPPGAVE + h.getReferanseId())
            && !lager.erOpprettet(OPPGAVE + h.getReferanseId())) {
            var key = nøkkel(h.getFnr(), h.getReferanseId(), h.getSaksnummer());
            LOG.info("Oppretter oppgave med eventId: {}, saksnummer: {}, tekst: {} i Ditt Nav",
                key.getEventId(), h.getSaksnummer(), tekst);
            send(oppgave(tekst, config.uri(), config.getOppgaveVarighet()), key, config.getOppgave());
            lager.opprettOppgave(h.getFnr(), key.getGrupperingsId(), h.getReferanseId(), h.getReferanseId());
        } else {
            LOG.info("Det er allerede opprettet en oppgave for referanseId {} ", h.getReferanseId());
        }
    }

    private void send(Object msg, NokkelInput key, String topic) {
        var melding = new ProducerRecord<NokkelInput, Object>(topic, key, msg);
        LOG.info("Sender melding med id {} på {}", key.getEventId(), topic);
        kafkaOperations.send(melding).addCallback(new ListenableFutureCallback<SendResult<NokkelInput, Object>>() {

            @Override
            public void onSuccess(SendResult<NokkelInput, Object> result) {
                LOG.info("Sendte melding med id {} og offset {} på {}", key.getEventId(),
                    result.getRecordMetadata().offset(), topic);
            }

            @Override
            public void onFailure(Throwable e) {
                LOG.warn("Kunne ikke sende melding {} med id {} på {}", msg, key.getEventId(), topic, e);
            }
        });
    }

    @Override
    public String toString() {
        return "DittNavMeldingProdusent{" +
            "config=" + config +
            ", lager=" + lager +
            ", kafkaOperations=" + kafkaOperations +
            '}';
    }
}
