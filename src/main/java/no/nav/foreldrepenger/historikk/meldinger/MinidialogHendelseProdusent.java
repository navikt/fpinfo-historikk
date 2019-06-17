package no.nav.foreldrepenger.historikk.meldinger;

import static no.nav.foreldrepenger.historikk.config.TxConfiguration.KAFKA;
import static no.nav.foreldrepenger.historikk.util.EnvUtil.DEV;
import static no.nav.foreldrepenger.historikk.util.EnvUtil.PREPROD;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import no.nav.foreldrepenger.historikk.config.Constants;
import no.nav.foreldrepenger.historikk.domain.MinidialogInnslag;
import no.nav.foreldrepenger.historikk.util.MDCUtil;

@Service
@Profile({ DEV, PREPROD })
public class MinidialogHendelseProdusent {
    private static final Logger LOG = LoggerFactory.getLogger(MinidialogHendelseProdusent.class);
    private final String topic;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Inject
    private ObjectMapper mapper;

    public MinidialogHendelseProdusent(@Value("${historikk.kafka.meldinger.topic}") String topic,
            KafkaTemplate<String, String> kafkaTemplate) {
        this.topic = topic;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Transactional(KAFKA)
    public void sendMinidialogHendelse(MinidialogInnslag hendelse) throws JsonProcessingException {
        Message<String> message = MessageBuilder
                .withPayload(mapper.writeValueAsString(hendelse))
                .setHeader(KafkaHeaders.TOPIC, topic)
                .setHeader(Constants.NAV_CALL_ID, MDCUtil.callIdOrNew())
                .build();
        LOG.info(String.format("Sender hendelse %s", message));
        kafkaTemplate.send(message);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [topic=" + topic + ", kafkaTemplate=" + kafkaTemplate + "]";
    }
}
