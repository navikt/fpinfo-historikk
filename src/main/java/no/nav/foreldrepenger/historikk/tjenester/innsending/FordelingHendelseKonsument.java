package no.nav.foreldrepenger.historikk.tjenester.innsending;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@ConditionalOnProperty(name = "historikk.fordeling.enabled", havingValue = "true")
public class FordelingHendelseKonsument {

    private static final Logger LOG = LoggerFactory.getLogger(FordelingHendelseKonsument.class);

    private final InnsendingTjeneste innsending;

    public FordelingHendelseKonsument(InnsendingTjeneste innsending) {
        this.innsending = innsending;
    }

    @Transactional
    @KafkaListener(topics = "#{'${historikk.kafka.topics.fordeling}'}", groupId = "#{'${spring.kafka.consumer.group-id}'}")
    public void behandle(@Payload @Valid InnsendingFordeltOgJournalførtHendelse h) {
        LOG.info("Mottok fordelingshendelse {}", h);
        innsending.fordel(h);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[innsending=" + innsending + "]";
    }
}