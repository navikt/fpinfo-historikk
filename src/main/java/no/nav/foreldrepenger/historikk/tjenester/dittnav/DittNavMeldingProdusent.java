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
import org.springframework.util.concurrent.ListenableFutureCallback;

import no.nav.foreldrepenger.historikk.domain.Fødselsnummer;
import no.nav.foreldrepenger.historikk.tjenester.innsending.InnsendingHendelse;

@Service
public class DittNavMeldingProdusent implements DittNav {

    private static final Logger LOG = LoggerFactory.getLogger(DittNavMeldingProdusent.class);

    private final KafkaOperations<NokkelInput, Object> kafka;
    private final DittNavConfig config;
    private final DittNavMeldingsHistorikk lager;

    public DittNavMeldingProdusent(KafkaOperations<NokkelInput, Object> kafka,
            DittNavConfig config, DittNavMeldingsHistorikk lager) {
        this.kafka = kafka;
        this.config = config;
        this.lager = lager;
    }

    @Override
    public void avsluttBeskjed(Fødselsnummer fnr, String grupperingsId, String eventId) {
        var key = beskjedNøkkel(fnr, eventId, grupperingsId);
        if (lager.slett(key.getEventId())) {
            LOG.info("Avslutter beskjed med eventId {} for {} {} i Ditt Nav", key.getEventId(), fnr, grupperingsId);
            send(avslutt(), key, config.getDone());
        } else {
            LOG.info("Ingen beskjed å avslutte i Ditt Nav");
        }
    }

    @Override
    public void avsluttOppgave(Fødselsnummer fnr, String grupperingsId, String eventId) {
        var key = oppgaveNøkkel(fnr, eventId, grupperingsId);
        if (lager.slett(key.getEventId())) {
            LOG.info("Avslutter oppgave med eventId  {} for {} {} i Ditt Nav", key.getEventId(), fnr, grupperingsId);
            send(avslutt(), key, config.getDone());
        } else {
            LOG.info("Ingen oppgave å avslutte i Ditt Nav");
        }
    }

    @Override
    public void opprettBeskjed(InnsendingHendelse h, String tekst) {
        var key = beskjedNøkkel(h.getFnr(), h.getReferanseId(), h.getSaksnummer());
        if (!lager.erOpprettet(key.getEventId())) {
            if (h.getSaksnummer() != null) {
                LOG.info("Oppretter beskjed for med eventId {} {} {} {} i Ditt Nav", key.getEventId(), h.getFnr(), h.getSaksnummer(),
                        tekst);
                send(beskjedInput(tekst, config.uri(), config.getVarighet()), key, config.getBeskjed());
            } else {
                LOG.info("Kan ikke gruppere beskjed i Ditt Nav uten grupperingsId(saksnr), bruker random verdi");
                key = beskjedNøkkel(h.getFnr(), h.getReferanseId(), UUID.randomUUID().toString());
                send(beskjedInput(tekst, config.uri(), config.getVarighet()), key, config.getBeskjed());
            }
            lager.opprett(h.getFnr(), key.getEventId());
        } else {
            LOG.info("Det er allerde opprettet en beskjed {} ", key.getEventId());
        }
    }

    @Override
    public void opprettOppgave(InnsendingHendelse h, String tekst) {
        var key = oppgaveNøkkel(h.getFnr(), h.getReferanseId(), h.getSaksnummer());
        if (!lager.erOpprettet(key.getEventId())) {
            LOG.info("Oppretter oppgave med eventId {} {} {} {} i Ditt Nav", key.getEventId(), h.getFnr(), h.getSaksnummer(),
                    tekst);
            send(oppgaveInput(tekst, config.uri()), key, config.getOppgave());
            lager.opprett(h.getFnr(), key.getEventId());
        } else {
            LOG.info("Det er allerede opprettet en oppgave {} ", key.getEventId());
        }
    }

    private void send(Object msg, NokkelInput key, String topic) {
        var melding = new ProducerRecord<>(topic, key, msg);
        LOG.info("Sender melding med id {} på {}", key.getEventId(), topic);
        kafka.send(melding).addCallback(new ListenableFutureCallback<>() {

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
        return getClass().getSimpleName() + " [kafka=" + kafka + ", config=" + config + ", lager=" + lager + "]";
    }

}
