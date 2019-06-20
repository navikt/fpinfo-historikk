package no.nav.foreldrepenger.historikk.tjenester.innsending;

import static no.nav.foreldrepenger.historikk.config.Constants.NAV_CALL_ID;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import no.nav.foreldrepenger.historikk.tjenester.historikk.HistorikkTjeneste;
import no.nav.foreldrepenger.historikk.tjenester.minidialog.MinidialogTjeneste;
import no.nav.foreldrepenger.historikk.util.JacksonUtil;

@Service
public class InnsendingEventKonsument {

    private final HistorikkTjeneste historikk;
    private final MinidialogTjeneste dialog;
    private final JacksonUtil mapper;

    public InnsendingEventKonsument(HistorikkTjeneste historikk, MinidialogTjeneste dialog, JacksonUtil mapper) {
        this.historikk = historikk;
        this.dialog = dialog;
        this.mapper = mapper;
    }

    @Transactional
    @KafkaListener(topics = "#{'${historikk.kafka.meldinger.søknad_topic}'}", groupId = "#{'${spring.kafka.consumer.group-id}'}")
    public void listen(String json, @Header(required = false, value = NAV_CALL_ID) String callId) {
        InnsendingEvent event = mapper.convertTo(json, InnsendingEvent.class);
        historikk.lagre(event);
        dialog.deaktiverMinidialoger(event);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[historikk=" + historikk + ", dialog=" + dialog + ", mapper=" + mapper
                + "]";
    }

}