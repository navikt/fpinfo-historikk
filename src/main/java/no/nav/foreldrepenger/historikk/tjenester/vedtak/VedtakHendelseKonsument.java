package no.nav.foreldrepenger.historikk.tjenester.vedtak;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import no.nav.abakus.vedtak.ytelse.v1.YtelseV1;

@Service
@ConditionalOnProperty(name = "historikk.vedtak.enabled", havingValue = "true")
public class VedtakHendelseKonsument {

    private static final Logger LOG = LoggerFactory.getLogger(VedtakHendelseKonsument.class);

    @Transactional
    @KafkaListener(topics = "#{'${historikk.vedtak.topic}'}", groupId = "#{'${spring.kafka.consumer.group-id}'}")
    public void behandle(@Payload @Valid YtelseV1 h) {
        LOG.info("Mottok vedtakshendelse {}", h);
    }

}