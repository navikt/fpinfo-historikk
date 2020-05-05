package no.nav.foreldrepenger.historikk.tjenester.dittnav;

import static no.nav.foreldrepenger.boot.conditionals.EnvUtil.isDevOrLocal;
import static no.nav.foreldrepenger.historikk.config.TxConfiguration.KAFKA_TM;
import static no.nav.foreldrepenger.historikk.tjenester.dittnav.DittNavMapper.beskjed;
import static no.nav.foreldrepenger.historikk.tjenester.dittnav.DittNavMapper.oppgave;

import java.time.Duration;
import java.util.UUID;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.kafka.core.KafkaOperations;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.concurrent.ListenableFutureCallback;

import no.nav.brukernotifikasjon.schemas.Nokkel;
import no.nav.foreldrepenger.historikk.domain.Fødselsnummer;

@Service
@ConditionalOnProperty(name = "historikk.dittnav.enabled", havingValue = "true")
public class DittNavMeldingProdusent implements DittNav, EnvironmentAware {

    private static final String SYSTEMBRUKER = "srvfpinfo-historikk";

    private static final Logger LOG = LoggerFactory.getLogger(DittNavMeldingProdusent.class);

    private final KafkaOperations<Nokkel, Object> kafkaOperations;

    private final DittNavConfig config;

    private final Duration varighet;

    private Environment env;

    public DittNavMeldingProdusent(KafkaOperations<Nokkel, Object> kafkaOperations,
            DittNavConfig config, @Value("${dittnav.beskjed.levetid:90d}") Duration varighet) {
        this.kafkaOperations = kafkaOperations;
        this.config = config;
        this.varighet = varighet;
    }

    @Transactional(KAFKA_TM)
    @Override
    public void avslutt(Fødselsnummer fnr, String grupperingsId,
            String eventId) {
        LOG.info("Avslutter oppgave med eventId  {} for {} {} i Ditt Nav", eventId, fnr, grupperingsId);
        if (isDevOrLocal(env)) {
            send(DittNavMapper.avslutt(fnr, grupperingsId), eventId, config.getDone());
        } else {
            LOG.info("Avslutter ikke oppgaver/beskjeder i prod foreløpig");
        }
    }

    @Override
    @Transactional(KAFKA_TM)
    public void opprettBeskjed(Fødselsnummer fnr, String grupperingsId, String eventId, String tekst, String url) {
        if (grupperingsId != null) {
            LOG.info("Oppretter beskjed med eventId {} for {} {} {} {} i Ditt Nav", eventId, fnr, grupperingsId, tekst,
                    url);
            send(beskjed(fnr, grupperingsId, tekst, url, varighet), eventId, config.getBeskjed());
        } else {
            LOG.info("Kan ikke gruppere beskjed i Ditt Nav uten grupperingsId(saksnr), bruker random verdi");
            send(beskjed(fnr, UUID.randomUUID().toString(), tekst, url, varighet), eventId,
                    config.getBeskjed());
        }
    }

    @Override
    @Transactional(KAFKA_TM)
    public void opprettOppgave(Fødselsnummer fnr, String grupperingsId, String eventId, String tekst, String url) {
        LOG.info("Oppretter oppgave for med eventId {} {} {} {} {} i Ditt Nav", eventId, fnr, grupperingsId, tekst,
                url);
        if (isDevOrLocal(env)) {
            send(oppgave(fnr, grupperingsId, tekst, url), eventId, config.getOppgave());
        } else {
            LOG.info("Lager ikke oppgaver i prod foreløpig");
        }

    }

    private void send(Object msg, String eventId, String topic) {
        ProducerRecord<Nokkel, Object> melding = new ProducerRecord<>(topic, beskjedNøkkel(eventId), msg);
        LOG.info("Sender melding med id {} på {}", eventId, topic);
        kafkaOperations.send(melding).addCallback(new ListenableFutureCallback<SendResult<Nokkel, Object>>() {

            @Override
            public void onSuccess(SendResult<Nokkel, Object> result) {
                LOG.info("Sendte melding med id {} og offset {} på {}", eventId,
                        result.getRecordMetadata().offset(), topic);
            }

            @Override
            public void onFailure(Throwable e) {
                LOG.warn("Kunne ikke sende melding med id {} på {}", eventId, topic, e);
            }
        });
    }

    private static Nokkel beskjedNøkkel(String eventId) {
        return Nokkel.newBuilder()
                .setEventId(eventId)
                .setSystembruker(SYSTEMBRUKER)
                .build();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + ", kafkaOperations=" + kafkaOperations + ", config=" + config + "]";
    }

    @Override
    public void setEnvironment(Environment env) {
        this.env = env;
    }

}
