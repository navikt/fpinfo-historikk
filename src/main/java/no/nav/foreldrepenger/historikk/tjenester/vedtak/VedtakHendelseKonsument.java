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
import no.nav.foreldrepenger.historikk.tjenester.dittnav.DittNav;
import no.nav.foreldrepenger.historikk.tjenester.oppslag.Oppslag;

@Service
@ConditionalOnProperty(name = "historikk.vedtak.enabled", havingValue = "true")
public class VedtakHendelseKonsument {

    private static final Logger LOG = LoggerFactory.getLogger(VedtakHendelseKonsument.class);

    private final Oppslag oppslag;
    private final DittNav dittNav;
    private final VedtakTjeneste vedtak;

    public VedtakHendelseKonsument(VedtakTjeneste vedtak, DittNav dittNav, Oppslag oppslag) {
        this.dittNav = dittNav;
        this.oppslag = oppslag;
        this.vedtak = vedtak;
    }

    @Transactional
    @KafkaListener(topics = "#{'${historikk.kafka.topics.vedtak}'}", groupId = "#{'${spring.kafka.consumer.group-id}'}")
    public void behandle(@Payload @Valid YtelseV1 h) {
        LOG.info("Mottok vedtakshendelse {}", h);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[dittNav=" + dittNav + "]";
    }

}