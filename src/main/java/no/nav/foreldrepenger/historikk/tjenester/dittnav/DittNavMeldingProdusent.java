package no.nav.foreldrepenger.historikk.tjenester.dittnav;

import static no.nav.foreldrepenger.historikk.config.TxConfiguration.KAFKA_TM;
import static no.nav.foreldrepenger.historikk.tjenester.dittnav.DittNavMapper.beskjed;
import static no.nav.foreldrepenger.historikk.tjenester.dittnav.DittNavMapper.done;
import static no.nav.foreldrepenger.historikk.tjenester.dittnav.DittNavMapper.oppgave;

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
import no.nav.foreldrepenger.historikk.tjenester.innsending.InnsendingHendelse;
import no.nav.foreldrepenger.historikk.tjenester.minidialog.MinidialogHendelse;
import no.nav.foreldrepenger.historikk.util.EnvUtil;

@Service
@ConditionalOnProperty(name = "historikk.dittnav.enabled", havingValue = "true")
public class DittNavMeldingProdusent implements DittNavOperasjoner, EnvironmentAware {

    private static final String SYSTEMBRUKER = "srvfpinfo-historikk";

    private static final Logger LOG = LoggerFactory.getLogger(DittNavMeldingProdusent.class);

    private final KafkaOperations<Nokkel, Object> kafkaOperations;
    private final String opprettOppgaveTopic;
    private final String avsluttOppgaveTopic;
    private final String beskjedTopic;

    private Environment env;

    public DittNavMeldingProdusent(KafkaOperations<Nokkel, Object> kafkaOperations,
            @Value("${historikk.kafka.topics.oppgave.opprett}") String opprettOppgaveTopic,
            @Value("${historikk.kafka.topics.oppgave.done}") String avsluttOppgaveTopic,
            @Value("${historikk.kafka.topics.beskjed}") String beskjedTopic) {
        this.kafkaOperations = kafkaOperations;
        this.opprettOppgaveTopic = opprettOppgaveTopic;
        this.avsluttOppgaveTopic = avsluttOppgaveTopic;
        this.beskjedTopic = beskjedTopic;
    }

    @Transactional(KAFKA_TM)
    @Override
    public void avsluttOppgave(Fødselsnummer fnr, String grupperingsId, String eventId) {
        send(done(fnr, grupperingsId), eventId, avsluttOppgaveTopic);
    }

    @Transactional(KAFKA_TM)
    @Override
    public void opprettBeskjed(InnsendingHendelse h) {
        send(beskjed(h, url()), h.getReferanseId(), beskjedTopic);
    }

    @Override
    @Transactional(KAFKA_TM)
    public void opprettOppgave(MinidialogHendelse h) {
        send(oppgave(h, url()), h.getDialogId(), opprettOppgaveTopic);
    }

    private String url() {
        // TODO
        return EnvUtil.isDev(env) ? "https://foreldrepengesoknad-q.nav.no/" : "https://foreldrepengesoknad.nav.no";
    }

    private void send(Object msg, String eventId, String topic) {
        ProducerRecord<Nokkel, Object> melding = new ProducerRecord<>(topic, nøkkel(eventId), msg);
        kafkaOperations.send(melding).addCallback(new ListenableFutureCallback<SendResult<Nokkel, Object>>() {

            @Override
            public void onSuccess(SendResult<Nokkel, Object> result) {
                LOG.info("Sendte melding {} med offset {} på {}", melding.value(),
                        result.getRecordMetadata().offset(), topic);
            }

            @Override
            public void onFailure(Throwable e) {
                LOG.warn("Kunne ikke sende melding {} på {}", melding.value(), topic, e);
            }
        });
    }

    private static Nokkel nøkkel(String eventId) {
        var nøkkel = Nokkel.newBuilder()
                .setEventId(eventId)
                .setSystembruker(SYSTEMBRUKER)
                .build();
        LOG.info("Bruker nøkkel med eventId {} og systembruker {}", nøkkel.getEventId(), nøkkel.getSystembruker());
        return nøkkel;
    }

    @Override
    public void setEnvironment(Environment env) {
        this.env = env;
    }
}
