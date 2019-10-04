package no.nav.foreldrepenger.historikk.tjenester.felles;

import static no.nav.foreldrepenger.historikk.config.Constants.NAV_CALL_ID;
import static no.nav.foreldrepenger.historikk.config.TxConfiguration.KAFKA_TM;
import static no.nav.foreldrepenger.historikk.util.MDCUtil.callIdOrNew;
import static org.springframework.kafka.support.KafkaHeaders.TOPIC;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaOperations;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.concurrent.ListenableFutureCallback;

import no.nav.foreldrepenger.historikk.util.ObjectMapperWrapper;

public abstract class AbstractHendelseProdusent<T extends Hendelse> {
    private static final Logger LOG = LoggerFactory.getLogger(AbstractHendelseProdusent.class);
    private final String topic;
    private final KafkaOperations<String, String> kafkaOperations;
    private final ObjectMapperWrapper mapper;

    public AbstractHendelseProdusent(String topic, KafkaOperations<String, String> kafkaOperations,
            ObjectMapperWrapper mapper) {
        this.topic = topic;
        this.kafkaOperations = kafkaOperations;
        this.mapper = mapper;
    }

    @Transactional(KAFKA_TM)
    public void send(T hendelse) {
        send(MessageBuilder
                .withPayload(payload(hendelse))
                .setHeader(TOPIC, topic)
                .setHeader(NAV_CALL_ID, callIdOrNew())
                .build());
    }

    private String payload(T hendelse) {
        String payload = mapper.writeValueAsString(hendelse);
        LOG.info("Konvertert til  payload {} fra hendelse {}", payload, hendelse);
        return payload;
    }

    private void send(Message<String> message) {
        LOG.info("Sender melding {}", message);
        kafkaOperations.send(message)
                .addCallback(new ListenableFutureCallback<SendResult<String, String>>() {

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

}
