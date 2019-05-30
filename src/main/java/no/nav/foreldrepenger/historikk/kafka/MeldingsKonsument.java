package no.nav.foreldrepenger.historikk.kafka;

import static no.nav.foreldrepenger.historikk.util.EnvUtil.DEV;
import static no.nav.foreldrepenger.historikk.util.EnvUtil.PREPROD;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

import no.nav.foreldrepenger.historikk.domain.Melding;
import no.nav.foreldrepenger.historikk.meldingslager.MeldingsLagerTjeneste;
import no.nav.foreldrepenger.historikk.util.JacksonUtil;

@Service
@Profile({ DEV, PREPROD })
public class MeldingsKonsument {
    private static final Logger LOG = LoggerFactory.getLogger(MeldingsKonsument.class);
    private final MeldingsLagerTjeneste meldingsLager;
    private final JacksonUtil mapper;

    public MeldingsKonsument(MeldingsLagerTjeneste meldingsLager, JacksonUtil mapper) {
        this.meldingsLager = meldingsLager;
        this.mapper = mapper;
    }

    @KafkaListener(topics = "#{'${kafka.topic}'}", groupId = "#{'${spring.kafka.consumer.group-id}'}")
    public void listen(String json, Acknowledgment ack) {
        Melding melding = mapper.convert(json, Melding.class);
        LOG.info("Mottok melding -> {}", json);
        meldingsLager.lagre(melding);
        ack.acknowledge();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[meldingsLager=" + meldingsLager + ", mapper=" + mapper + "]";
    }
}