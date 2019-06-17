package no.nav.foreldrepenger.historikk.meldinger;

import static no.nav.foreldrepenger.historikk.config.Constants.NAV_CALL_ID;
import static no.nav.foreldrepenger.historikk.util.EnvUtil.DEV;
import static no.nav.foreldrepenger.historikk.util.EnvUtil.PREPROD;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import no.nav.foreldrepenger.historikk.domain.MinidialogInnslag;
import no.nav.foreldrepenger.historikk.util.JacksonUtil;

@Service
@Profile({ DEV, PREPROD })
public class MinidialogEventKonsument {
    private static final Logger LOG = LoggerFactory.getLogger(MinidialogEventKonsument.class);
    private final MinidialogTjeneste meldingsLager;
    private final JacksonUtil mapper;

    public MinidialogEventKonsument(MinidialogTjeneste meldingsLager, JacksonUtil mapper) {
        this.meldingsLager = meldingsLager;
        this.mapper = mapper;
    }

    @KafkaListener(topics = "#{'${historikk.kafka.meldinger.topic}'}", groupId = "#{'${spring.kafka.consumer.group-id}'}")
    @Transactional
    public void listen(String json, @Header(required = false, value = NAV_CALL_ID) String callId) {
        MinidialogInnslag melding = mapper.convertTo(json, MinidialogInnslag.class);
        LOG.info("Mottok melding {} {}", json, callId);
        meldingsLager.lagre(melding);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[meldingsLager=" + meldingsLager + ", mapper=" + mapper + "]";
    }
}