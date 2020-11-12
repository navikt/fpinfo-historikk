package no.nav.foreldrepenger.historikk.tjenester.dittnav;

import static no.nav.foreldrepenger.historikk.config.TxConfiguration.KAFKA_TM;
import static no.nav.foreldrepenger.historikk.tjenester.dittnav.DittNavMapper.avslutt;
import static no.nav.foreldrepenger.historikk.tjenester.dittnav.DittNavMapper.beskjed;
import static no.nav.foreldrepenger.historikk.tjenester.dittnav.DittNavMapper.beskjedNøkkel;
import static no.nav.foreldrepenger.historikk.tjenester.dittnav.DittNavMapper.oppgave;
import static no.nav.foreldrepenger.historikk.tjenester.dittnav.DittNavMapper.oppgaveNøkkel;

import java.time.Duration;
import java.util.UUID;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaOperations;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.concurrent.ListenableFutureCallback;

import no.nav.brukernotifikasjon.schemas.Nokkel;
import no.nav.foreldrepenger.historikk.domain.Fødselsnummer;
import no.nav.foreldrepenger.historikk.tjenester.innsending.DittNavOppgave;

@Service
@ConditionalOnProperty(name = "historikk.dittnav.enabled", havingValue = "true")
public class DittNavMeldingProdusent implements DittNav {

    private static final Logger LOG = LoggerFactory.getLogger(DittNavMeldingProdusent.class);

    private final KafkaOperations<Nokkel, Object> kafkaOperations;

    private final DittNavConfig config;

    private final DittNavOppgave oppgave;

    private final Duration varighet;

    public DittNavMeldingProdusent(KafkaOperations<Nokkel, Object> kafkaOperations,
            DittNavConfig config, DittNavOppgave oppgave, @Value("${dittnav.beskjed.levetid:90d}") Duration varighet) {
        this.kafkaOperations = kafkaOperations;
        this.config = config;
        this.varighet = varighet;
        this.oppgave = oppgave;
    }

    @Transactional(KAFKA_TM)
    @Override
    public void avsluttOppgave(Fødselsnummer fnr, String grupperingsId,
            String eventId) {
        var key = oppgaveNøkkel(eventId);
        LOG.info("Avslutter oppgave med eventId  {} for {} {} i Ditt Nav", key.getEventId(), fnr, grupperingsId);
        send(avslutt(fnr, grupperingsId), key, config.getDone());
    }

    @Transactional(KAFKA_TM)
    @Override
    public void avsluttBeskjed(Fødselsnummer fnr, String grupperingsId, String eventId) {
        var key = beskjedNøkkel(eventId);
        LOG.info("Avslutter beskjed med eventId  {} for {} {} i Ditt Nav", key.getEventId(), fnr, grupperingsId);
        send(avslutt(fnr, grupperingsId), key, config.getDone());
    }

    @Override
    @Transactional(KAFKA_TM)
    public void opprettBeskjed(Fødselsnummer fnr, String grupperingsId, String eventId, String tekst, String url) {
        var key = beskjedNøkkel(eventId);
        if (grupperingsId != null) {
            LOG.info("Oppretter beskjed med eventId {} for {} {} {} {} i Ditt Nav", key.getEventId(), fnr,
                    grupperingsId, tekst,
                    url);
            send(beskjed(fnr, grupperingsId, tekst, url, varighet), key, config.getBeskjed());
        } else {
            LOG.info("Kan ikke gruppere beskjed i Ditt Nav uten grupperingsId(saksnr), bruker random verdi");
            send(beskjed(fnr, UUID.randomUUID().toString(), tekst, url, varighet), key,
                    config.getBeskjed());
        }
    }

    @Override
    @Transactional(KAFKA_TM)
    public void opprettOppgave(Fødselsnummer fnr, String grupperingsId, String eventId, String tekst, String url, String saksnr) {
        var key = oppgaveNøkkel(eventId);
        LOG.info("Oppretter oppgave for med eventId {} {} {} {} {} i Ditt Nav", key.getEventId(), fnr, grupperingsId,
                tekst,
                url);

        send(oppgave(fnr, grupperingsId, tekst, url), key, config.getOppgave());
        oppgave.opprett(fnr, eventId, saksnr);
    }

    private void send(Object msg, Nokkel key, String topic) {
        var melding = new ProducerRecord<>(topic, key, msg);
        LOG.info("Sender melding med id {} på {}", key.getEventId(), topic);
        kafkaOperations.send(melding).addCallback(new ListenableFutureCallback<SendResult<Nokkel, Object>>() {

            @Override
            public void onSuccess(SendResult<Nokkel, Object> result) {
                LOG.info("Sendte melding med id {} og offset {} på {}", key.getEventId(),
                        result.getRecordMetadata().offset(), topic);
            }

            @Override
            public void onFailure(Throwable e) {
                LOG.warn("Kunne ikke sende melding med id {} på {}", key.getEventId(), topic, e);
            }
        });
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + ", kafkaOperations=" + kafkaOperations + ", config=" + config + "]";
    }

}
