package no.nav.foreldrepenger.historikk.tjenester.innsending;

import static no.nav.foreldrepenger.historikk.config.Constants.NAV_CALL_ID;
import static no.nav.foreldrepenger.historikk.config.TxConfiguration.KAFKA_TM;
import static no.nav.foreldrepenger.historikk.util.EnvUtil.DEV;
import static no.nav.foreldrepenger.historikk.util.EnvUtil.LOCAL;
import static no.nav.foreldrepenger.historikk.util.MDCUtil.callIdOrNew;
import static org.springframework.kafka.support.KafkaHeaders.TOPIC;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.KafkaOperations;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.concurrent.ListenableFutureCallback;

import no.nav.foreldrepenger.historikk.util.JacksonUtil;

@Service
@Profile({ LOCAL, DEV })
public class InnsendingHendelseProdusent {
    private static final Logger LOG = LoggerFactory.getLogger(InnsendingHendelseProdusent.class);
    private final String topicNavn;
    private final KafkaOperations<String, String> kafkaOperations;
    private final JacksonUtil mapper;

    public InnsendingHendelseProdusent(KafkaOperations<String, String> kafkaOperations,
            @Value("${historikk.kafka.meldinger.søknad_topic}") String topicNavn, JacksonUtil mapper) {
        this.topicNavn = topicNavn;
        this.kafkaOperations = kafkaOperations;
        this.mapper = mapper;
    }

    @Transactional(KAFKA_TM)
    public void sendInnsendingHendelse(SøknadInnsendingHendelse hendelse) {
        LOG.info("Sender event {}", hendelse);
        Message<String> message = MessageBuilder
                .withPayload(mapper.writeValueAsString(hendelse))
                .setHeader(TOPIC, topicNavn)
                .setHeader(NAV_CALL_ID, callIdOrNew())
                .build();
        send(message);
    }

    private void send(Message<String> message) {
        LOG.info("Sender melding {}", message);
        kafkaOperations.send(message).addCallback(new ListenableFutureCallback<SendResult<String, String>>() {

            @Override
            public void onSuccess(SendResult<String, String> result) {
                LOG.info("Sendte melding {} med offset {}", message,
                        result.getRecordMetadata().offset());
            }

            @Override
            public void onFailure(Throwable e) {
                LOG.warn("Kunne ikke sende melding {}", message, e);
            }
        });
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[topicNavn=" + topicNavn + ", kafkaOperations=" + kafkaOperations
                + ", mapper=" + mapper + "]";
    }

}
