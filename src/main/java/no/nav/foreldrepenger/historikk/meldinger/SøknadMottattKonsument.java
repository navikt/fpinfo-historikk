package no.nav.foreldrepenger.historikk.meldinger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class SøknadMottattKonsument {
    private static final Logger LOG = LoggerFactory.getLogger(SøknadMottattKonsument.class);

    @KafkaListener(topics = "#{'${historikk.kafka.meldinger.søknad_topic}'}", groupId = "#{'${spring.kafka.consumer.group-id}'}")
    public void listen(String json) {
        LOG.info("Mottok melding om søknad {}", json);
    }
}