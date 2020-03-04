package no.nav.foreldrepenger.historikk.tjenester.brev;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import no.nav.foreldrepenger.boot.conditionals.ConditionalOnNotProd;
import no.nav.foreldrepenger.historikk.tjenester.dittnav.DittNav;
import no.nav.historikk.v1.HistorikkInnslagV1;

@Service
@ConditionalOnNotProd
public class DokumentbestillingHendelseKonsument {

    private static final Logger LOG = LoggerFactory.getLogger(DokumentbestillingHendelseKonsument.class);
    private final DittNav dittNav;

    public DokumentbestillingHendelseKonsument(DittNav dittNav) {
        this.dittNav = dittNav;
    }

    @KafkaListener(topics = "#{'${historikk.kafka.topics.brev}'}", groupId = "#{'${spring.kafka.consumer.group-id}'}")
    @Transactional
    public void listen(@Payload @Valid HistorikkInnslagV1 h) {
        LOG.info("Mottok dokumentbestillingshendelse {}", h);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[dittNav=" + dittNav + "]";
    }
}