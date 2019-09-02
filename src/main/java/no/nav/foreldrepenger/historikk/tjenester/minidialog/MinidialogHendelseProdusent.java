package no.nav.foreldrepenger.historikk.tjenester.minidialog;

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

import no.nav.foreldrepenger.historikk.util.JacksonMapperWrapper;

@Service
@Profile({ LOCAL, DEV })
public class MinidialogHendelseProdusent {
    private static final Logger LOG = LoggerFactory.getLogger(MinidialogHendelseProdusent.class);
    private final String topicNavn;
    private final KafkaOperations<String, String> kafkaOperations;
    private final JacksonMapperWrapper mapper;

    public MinidialogHendelseProdusent(KafkaOperations<String, String> kafkaOperations,
            @Value("${historikk.kafka.meldinger.topic}") String topicNavn, JacksonMapperWrapper mapper) {
        this.topicNavn = topicNavn;
        this.kafkaOperations = kafkaOperations;
        this.mapper = mapper;
    }

    @Transactional(KAFKA_TM)
    public void sendMinidialogHendelse(MinidialogHendelse hendelse) {
        LOG.info("Mottatt hendelse {}", hendelse);
        Message<String> message = MessageBuilder
                .withPayload(mapper.writeValueAsString(hendelse))
                .setHeader(TOPIC, topicNavn)
                .setHeader(NAV_CALL_ID, callIdOrNew())
                .build();
        LOG.info("Sender melding {}", message);
        send(message);
    }

    private void send(Message<String> message) {
        kafkaOperations.send(message).addCallback(new ListenableFutureCallback<SendResult<String, String>>() {

            @Override
            public void onSuccess(SendResult<String, String> result) {
                LOG.info("Sendte melding {} med offset {}", message,
                        result.getRecordMetadata().offset());
            }

            @Override
            public void onFailure(Throwable ex) {
                LOG.warn("Kunne ikke sende melding {}", message, ex);
            }
        });
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[topicNavn=" + topicNavn + ", kafkaOperations=" + kafkaOperations
                + ", mapper=" + mapper + "]";
    }

}
