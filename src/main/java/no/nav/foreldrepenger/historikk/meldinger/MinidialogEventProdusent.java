package no.nav.foreldrepenger.historikk.meldinger;

import static no.nav.foreldrepenger.historikk.config.Constants.NAV_CALL_ID;
import static no.nav.foreldrepenger.historikk.config.TxConfiguration.KAFKA_TM;
import static no.nav.foreldrepenger.historikk.util.EnvUtil.DEV;
import static no.nav.foreldrepenger.historikk.util.EnvUtil.PREPROD;
import static no.nav.foreldrepenger.historikk.util.MDCUtil.callIdOrNew;
import static org.springframework.kafka.support.KafkaHeaders.TOPIC;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.KafkaOperations;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import no.nav.foreldrepenger.historikk.domain.MinidialogInnslag;
import no.nav.foreldrepenger.historikk.util.JacksonUtil;

@Service
@Profile({ DEV, PREPROD })
public class MinidialogEventProdusent {
    private static final Logger LOG = LoggerFactory.getLogger(MinidialogEventProdusent.class);
    private final String topic;
    private final KafkaOperations<String, String> kafkaOperations;

    @Inject
    private JacksonUtil mapper;

    public MinidialogEventProdusent(KafkaOperations<String, String> kafkaOperations,
            @Value("${historikk.kafka.meldinger.topic}") String topic) {
        this.topic = topic;
        this.kafkaOperations = kafkaOperations;
    }

    @Transactional(KAFKA_TM)
    public void sendMinidialogHendelse(MinidialogInnslag hendelse) {
        Message<String> message = MessageBuilder
                .withPayload(mapper.writeValueAsString(hendelse))
                .setHeader(TOPIC, topic)
                .setHeader(NAV_CALL_ID, callIdOrNew())
                .build();
        LOG.info("Sender hendelse {}", message);
        kafkaOperations.send(message);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [topic=" + topic + ", kafkaOperations=" + kafkaOperations + "]";
    }
}
