package no.nav.foreldrepenger.historikk.tjenester.vedtak;

import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import no.nav.abakus.vedtak.ytelse.v1.YtelseV1;
import no.nav.foreldrepenger.historikk.tjenester.dittnav.DittNavOperasjoner;

@Service
@ConditionalOnProperty(name = "historikk.vedtak.enabled", havingValue = "true")
public class VedtakHendelseKonsument {

    private static final Logger LOG = LoggerFactory.getLogger(VedtakHendelseKonsument.class);

    private final DittNavOperasjoner dittNav;

    public VedtakHendelseKonsument(DittNavOperasjoner dittNav) {
        this.dittNav = dittNav;
    }

    @Transactional
    @KafkaListener(topics = "#{'${historikk.kafka.topics.vedtak}'}", groupId = "#{'${spring.kafka.consumer.group-id}'}")
    public void behandle(@Payload @Valid YtelseV1 h) {
        LOG.info("Mottatt vedtakshendelse {} {} {} {} {}", h.getAktør().getVerdi(), h.getFagsystem().getKode(),
                h.getType().getKode(), h.getStatus().getKode(),
                Optional.ofNullable(h.getVedtattTidspunkt().toString()).orElse("Ikke satt"));
        // dittNav.opprettBeskjed("TODO", String grupperingsId, String tekst, String
        // url);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[dittNav=" + dittNav + "]";
    }

}