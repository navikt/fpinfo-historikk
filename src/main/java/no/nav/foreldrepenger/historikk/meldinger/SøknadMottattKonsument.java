package no.nav.foreldrepenger.historikk.meldinger;

import org.jboss.logging.MDC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import no.nav.foreldrepenger.historikk.config.Constants;

@Service
public class SøknadMottattKonsument {
    private static final Logger LOG = LoggerFactory.getLogger(SøknadMottattKonsument.class);

    @Transactional("kafka")
    @KafkaListener(topics = "#{'${historikk.kafka.meldinger.søknad_topic}'}", groupId = "#{'${spring.kafka.consumer.group-id}'}")
    public void listen(String json, @Header(Constants.NAV_CALL_ID) String callId) {
        MDC.put(Constants.NAV_CALL_ID, callId);
        LOG.info("Mottok melding om søknad {}", json);
    }
}