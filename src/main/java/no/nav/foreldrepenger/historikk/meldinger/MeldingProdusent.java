package no.nav.foreldrepenger.historikk.meldinger;

import static no.nav.foreldrepenger.historikk.util.EnvUtil.DEV;
import static no.nav.foreldrepenger.historikk.util.EnvUtil.PREPROD;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import no.nav.foreldrepenger.historikk.domain.Melding;

@Service
@Profile({ DEV, PREPROD })
public class MeldingProdusent {
    private static final Logger LOG = LoggerFactory.getLogger(MeldingProdusent.class);
    private final String topic;
    private final KafkaTemplate<String, Message<Melding>> kafkaTemplate;

    public MeldingProdusent(@Value("${no.nav.foreldrepenger.historikk.kafka.meldinger.topic}") String topic,
            KafkaTemplate<String, Message<Melding>> kafkaTemplate) {
        this.topic = topic;
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMelding(Melding melding) {
        Message<Melding> message = MessageBuilder
                .withPayload(melding)
                .setHeader("X-Custom-Header", "Sending Custom Header with Spring Kafka")
                .build();
        LOG.info(String.format("Sender melding %s", melding));
        this.kafkaTemplate.send(topic, message);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [topic=" + topic + ", kafkaTemplate=" + kafkaTemplate + "]";
    }
}
