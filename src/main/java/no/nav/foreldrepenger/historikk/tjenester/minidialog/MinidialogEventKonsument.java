package no.nav.foreldrepenger.historikk.tjenester.minidialog;

import static no.nav.foreldrepenger.historikk.config.Constants.CALL_ID;
import static no.nav.foreldrepenger.historikk.config.Constants.NAV_CALL_ID;
import static no.nav.foreldrepenger.historikk.tjenester.minidialog.MinidialogMapper.journalpostFra;
import static no.nav.foreldrepenger.historikk.util.EnvUtil.DEV;
import static no.nav.foreldrepenger.historikk.util.EnvUtil.PREPROD;
import static no.nav.foreldrepenger.historikk.util.MDCUtil.callId;

import org.jboss.logging.MDC;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import no.nav.foreldrepenger.historikk.http.CallIdGenerator;
import no.nav.foreldrepenger.historikk.tjenester.historikk.HistorikkTjeneste;
import no.nav.foreldrepenger.historikk.tjenester.journalføring.JournalføringTjeneste;
import no.nav.foreldrepenger.historikk.tjenester.journalføring.pdf.PDFGenerator;
import no.nav.foreldrepenger.historikk.tjenester.oppslag.OppslagTjeneste;
import no.nav.foreldrepenger.historikk.util.JacksonUtil;

@Service
@Profile({ DEV, PREPROD })
public class MinidialogEventKonsument {
    private final MinidialogTjeneste minidialog;
    private final OppslagTjeneste oppslag;
    private final JournalføringTjeneste journalføring;
    private final HistorikkTjeneste historikk;
    private final JacksonUtil mapper;
    private final PDFGenerator generator;

    private static final CallIdGenerator GEN = new CallIdGenerator();

    public MinidialogEventKonsument(HistorikkTjeneste historikk, OppslagTjeneste oppslag, MinidialogTjeneste minidialog,
            JournalføringTjeneste journalføring, PDFGenerator generator,
            JacksonUtil mapper) {
        this.historikk = historikk;
        this.oppslag = oppslag;
        this.minidialog = minidialog;
        this.journalføring = journalføring;
        this.generator = generator;
        this.mapper = mapper;
    }

    @KafkaListener(topics = "#{'${historikk.kafka.meldinger.topic}'}", groupId = "#{'${spring.kafka.consumer.group-id}'}")
    @Transactional
    public void listen(String json) {
        MDC.put(NAV_CALL_ID, GEN.create());
        MDC.put(CALL_ID, callId());
        MinidialogInnslag innslag = mapper.convertTo(json, MinidialogInnslag.class);
        minidialog.lagre(innslag);
        String id = journalføring.sluttfør(
                journalpostFra(innslag, generator.generate(innslag.getTekst())));
        historikk.lagre(innslag, id);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[minidialog=" + minidialog + ", oppslag=" + oppslag + ", journalføring="
                + journalføring + ", historikk=" + historikk + ", mapper=" + mapper + ", generator=" + generator + "]";
    }

}