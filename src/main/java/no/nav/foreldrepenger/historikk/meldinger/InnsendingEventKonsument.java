package no.nav.foreldrepenger.historikk.meldinger;

import static no.nav.foreldrepenger.historikk.config.Constants.NAV_CALL_ID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import no.nav.foreldrepenger.historikk.domain.AktørId;
import no.nav.foreldrepenger.historikk.meldinger.event.InnsendingEvent;
import no.nav.foreldrepenger.historikk.util.JacksonUtil;

@Service
public class InnsendingEventKonsument {
    private static final Logger LOG = LoggerFactory.getLogger(InnsendingEventKonsument.class);

    private final HistorikkTjeneste historikk;
    private final MinidialogTjeneste dialog;
    private final JacksonUtil mapper;

    public InnsendingEventKonsument(JacksonUtil mapper, HistorikkTjeneste historikk, MinidialogTjeneste dialog) {
        this.mapper = mapper;
        this.historikk = historikk;
        this.dialog = dialog;
    }

    @Transactional
    @KafkaListener(topics = "#{'${historikk.kafka.meldinger.søknad_topic}'}", groupId = "#{'${spring.kafka.consumer.group-id}'}")
    public void listen(String json, @Header(required = false, value = NAV_CALL_ID) String callId) {
        InnsendingEvent event = mapper.convertTo(json, InnsendingEvent.class);
        LOG.info("Lagrer historikkinnslag fra hendelse {}", event);
        historikk.lagre(event);
        LOG.info("Lagret historikkinnslag fra hendelse OK ");
        LOG.info("Deaktiverer eventuelle minidialoger for {}", event.getType());
        int antallDeaktivert = dialog.deaktiverMinidaloger(AktørId.valueOf(event.getAktørId()), event.getType());
        LOG.info("{} minidialoger ble deaktivert", antallDeaktivert);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[historikk=" + historikk + ", dialog=" + dialog + ", mapper=" + mapper
                + "]";
    }

}