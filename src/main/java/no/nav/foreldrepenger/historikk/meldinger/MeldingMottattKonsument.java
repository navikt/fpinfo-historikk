package no.nav.foreldrepenger.historikk.meldinger;

import static no.nav.foreldrepenger.historikk.util.EnvUtil.DEV;
import static no.nav.foreldrepenger.historikk.util.EnvUtil.PREPROD;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import no.nav.foreldrepenger.historikk.config.Constants;
import no.nav.foreldrepenger.historikk.domain.Melding;
import no.nav.foreldrepenger.historikk.util.JacksonUtil;

@Service
@Profile({ DEV, PREPROD })
public class MeldingMottattKonsument {
    private static final Logger LOG = LoggerFactory.getLogger(MeldingMottattKonsument.class);
    private final MeldingsLagerTjeneste meldingsLager;
    private final JacksonUtil mapper;

    public MeldingMottattKonsument(MeldingsLagerTjeneste meldingsLager, JacksonUtil mapper) {
        this.meldingsLager = meldingsLager;
        this.mapper = mapper;
    }

    @KafkaListener(topics = "#{'${historikk.kafka.meldinger.topic}'}", groupId = "#{'${spring.kafka.consumer.group-id}'}")
    @Transactional
    public void listen(String json, @Header(Constants.NAV_CALL_ID) String callId) {
        Melding melding = mapper.convert(json, Melding.class);
        LOG.info("Mottok melding {} {}", json, callId);
        meldingsLager.lagre(melding);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[meldingsLager=" + meldingsLager + ", mapper=" + mapper + "]";
    }
}