package no.nav.foreldrepenger.historikk.kafka;

import static no.nav.foreldrepenger.historikk.util.EnvUtil.DEV;
import static no.nav.foreldrepenger.historikk.util.EnvUtil.PREPROD;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import no.nav.foreldrepenger.historikk.domain.Melding;

@Service
@Profile({ DEV, PREPROD })
public class Producer {
    private static final Logger LOG = LoggerFactory.getLogger(Producer.class);
    private final String topic;
    private final KafkaTemplate<String, Melding> kafkaTemplate;

    public Producer(@Value("${kafka.topic}") String topic, KafkaTemplate<String, Melding> kafkaTemplate) {
        this.topic = topic;
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMelding(Melding melding) {
        LOG.info(String.format("#### -> Producing message -> %s", melding));
        this.kafkaTemplate.send(topic, melding);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [topic=" + topic + ", kafkaTemplate=" + kafkaTemplate + "]";
    }
}
