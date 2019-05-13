package no.nav.foreldrepenger.historikk.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//@Component
public class MessageListener {
    private static final Logger LOG = LoggerFactory.getLogger(MessageListener.class);

    // @KafkaListener(topics = "poc", groupId = "foo")
    public void listen(String message) {
        LOG.info("Received message in group 'foo': {}", message);
    }
}
