package no.nav.foreldrepenger.historikk.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
//@Profile(EnvUtil.DEV)
public class Consumer {
    private final Logger logger = LoggerFactory.getLogger(Producer.class);

    @KafkaListener(topics = "fpinfo-historikk", groupId = "group_id")
    public void consume(String message) {
        logger.info(String.format("#### -> Consumed message -> %s", message));
    }
}