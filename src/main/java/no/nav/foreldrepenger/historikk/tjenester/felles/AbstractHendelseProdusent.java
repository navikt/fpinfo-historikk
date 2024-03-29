package no.nav.foreldrepenger.historikk.tjenester.felles;

import static no.nav.foreldrepenger.common.util.Constants.NAV_CALL_ID;
import static no.nav.foreldrepenger.common.util.MDCUtil.callIdOrNew;
import static no.nav.foreldrepenger.common.util.StreamUtil.safeStream;
import static org.springframework.kafka.support.KafkaHeaders.TOPIC;

import java.util.List;

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
    private final KafkaOperations<Object, Object> kafkaOperations;
    private final ObjectMapperWrapper mapper;

    protected AbstractHendelseProdusent(String topic, KafkaOperations<Object, Object> kafkaOperations,
            ObjectMapperWrapper mapper) {
        this.topic = topic;
        this.kafkaOperations = kafkaOperations;
        this.mapper = mapper;
    }

    @Transactional
    public void send(List<T> hendelser) {
        LOG.info("Mottok {} hendelser", hendelser.size());
        safeStream(hendelser).forEach(this::send);
    }

    @Transactional
    public void send(T hendelse) {
        LOG.info("Mottok hendelse {}", hendelse);
        send(MessageBuilder
                .withPayload(mapper.writeValueAsString(hendelse))
                .setHeader(TOPIC, topic)
                .setHeader(NAV_CALL_ID, callIdOrNew())
                .build());
    }

    private void send(Message<String> message) {
        LOG.info("Sender melding {} på {}", message.getPayload(), topic);
        kafkaOperations.send(message).addCallback(new ListenableFutureCallback<>() {

                    @Override
                    public void onSuccess(SendResult<Object, Object> result) {
                        LOG.info("Sendte melding {} med offset {} på {}", message.getPayload(),
                            result.getRecordMetadata().offset(), topic);
                    }

                    @Override
                    public void onFailure(Throwable e) {
                        LOG.warn("Kunne ikke sende melding {} på {}", message.getPayload(), topic, e);
                    }
                });
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[topic=" + topic + ", kafkaOperations=" + kafkaOperations + ", mapper="
                + mapper + "]";
    }

}
