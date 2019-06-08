package no.nav.foreldrepenger.historikk.meldinger;

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
public class MeldingProdusent {
    private static final Logger LOG = LoggerFactory.getLogger(MeldingProdusent.class);
    private final String topic;
    private final KafkaTemplate<String, Melding> kafkaTemplate;

    public MeldingProdusent(@Value("${no.nav.foreldrepenger.historikk.kafka.meldinger.topic}") String topic,
            KafkaTemplate<String, Melding> kafkaTemplate) {
        this.topic = topic;
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMelding(Melding melding) {

        LOG.info(String.format("Sender melding %s på topic %s", melding, topic));
        this.kafkaTemplate.send(topic, melding);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [topic=" + topic + ", kafkaTemplate=" + kafkaTemplate + "]";
    }
}
