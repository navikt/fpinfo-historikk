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

import no.nav.foreldrepenger.historikk.tjenester.innsending.InnsendingHendelse;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Service
public class DittNavMeldingProdusent implements DittNav {

    private static final Logger LOG = LoggerFactory.getLogger(DittNavMeldingProdusent.class);
    private static final Logger SECURE_LOG = LoggerFactory.getLogger("secureLogger");

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
    public void avsluttOppgave(String eventId) {
        lager.hentAktivOppgave(eventId)
            .ifPresentOrElse(oppgave -> {
                lager.avslutt(oppgave);
                var key = avsluttNøkkel(oppgave.getFnr(), oppgave.getEksternReferanseId(), oppgave.getGrupperingsId());
                LOG.info("Avslutter oppgave med eventId: {}, grupperingsId: {}", key.getEventId(), key.getGrupperingsId());
                send(avslutt(), key, config.getDone());
            }, () -> LOG.info("Ingen oppgave å avslutte i Ditt Nav"));
    }

    @Override
    public void opprettBeskjed(InnsendingHendelse h, String tekst) {
        if (!lager.erOpprettetBeskjed(h.getReferanseId())) {
            var key = nøkkel(h.getFnr(), UUID.randomUUID().toString(), grupperingsId(h));
            LOG.info("Oppretter beskjed med eventId {}, internReferanseId {}, grupperingId {}, tekst {}",
                key.getEventId(), h.getReferanseId(), key.getGrupperingsId(), tekst);
            send(beskjed(tekst, config.uri(), config.getBeskjedVarighet()), key, config.getBeskjed());
            lager.opprettBeskjed(h.getFnr(), key.getGrupperingsId(), h.getReferanseId(), key.getEventId());
        } else {
            LOG.info("Det er allerede opprettet en beskjed {} ", h.getReferanseId());
        }
    }

    @Override
    public void opprettOppgave(InnsendingHendelse h, String tekst) {
        if (!lager.erOpprettetOppgave(h.getReferanseId())) {
            var key = nøkkel(h.getFnr(), UUID.randomUUID().toString(), h.getSaksnummer());
            LOG.info("Oppretter oppgave med eventId: {}, saksnummer: {}, tekst: {} i Ditt Nav",
                key.getEventId(), h.getSaksnummer(), tekst);
            send(oppgave(tekst, config.uri(), config.getOppgaveVarighet()), key, config.getOppgave());
            lager.opprettOppgave(h.getFnr(), key.getGrupperingsId(), h.getReferanseId(), key.getEventId());
        } else {
            LOG.info("Det er allerede opprettet en oppgave for referanseId {} ", h.getReferanseId());
        }
    }

    private void send(Object msg, NokkelInput key, String topic) {
        var melding = new ProducerRecord<>(topic, key, msg);
        LOG.info("Sender melding med id {} på {}", key.getEventId(), topic);
        kafkaOperations.send(melding).addCallback(new ListenableFutureCallback<>() {

            @Override
            public void onSuccess(SendResult<NokkelInput, Object> result) {
                LOG.info("Sendte melding med id {} og offset {} på {}", key.getEventId(),
                    result.getRecordMetadata().offset(), topic);
            }

            @Override
            public void onFailure(Throwable e) {
                LOG.warn("Kunne ikke sende kafkamelding. Må følges opp, se secure logg for detaljer.");
                SECURE_LOG.warn("Kunne ikke sende melding {} med id {} på {}", msg, key.getEventId(), topic, e);
            }
        });
    }

    private String grupperingsId(InnsendingHendelse h) {
        if (h.getSaksnummer() == null) {
            LOG.info("Hendelse har ikke tilknyttet saksnummer, benytter random grupperingId");
            return UUID.randomUUID().toString();
        }
        return h.getSaksnummer();
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
