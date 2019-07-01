package no.nav.foreldrepenger.historikk.tjenester.minidialog;

import static no.nav.foreldrepenger.historikk.config.Constants.CALL_ID;
import static no.nav.foreldrepenger.historikk.config.Constants.NAV_CALL_ID;
import static no.nav.foreldrepenger.historikk.tjenester.minidialog.MinidialogMapper.journalpostFra;
import static no.nav.foreldrepenger.historikk.util.EnvUtil.DEV;
import static no.nav.foreldrepenger.historikk.util.EnvUtil.PREPROD;

import javax.validation.Valid;

import org.jboss.logging.MDC;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import no.nav.foreldrepenger.historikk.tjenester.historikk.HistorikkTjeneste;
import no.nav.foreldrepenger.historikk.tjenester.journalføring.JournalføringTjeneste;
import no.nav.foreldrepenger.historikk.tjenester.journalføring.pdf.PDFGenerator;
import no.nav.foreldrepenger.historikk.tjenester.oppslag.OppslagTjeneste;

@Service
@Profile({ DEV, PREPROD })
public class MinidialogEventKonsument {
    private final MinidialogTjeneste minidialog;
    private final OppslagTjeneste oppslag;
    private final JournalføringTjeneste journalføring;
    private final HistorikkTjeneste historikk;
    private final PDFGenerator generator;

    public MinidialogEventKonsument(HistorikkTjeneste historikk, OppslagTjeneste oppslag, MinidialogTjeneste minidialog,
            JournalføringTjeneste journalføring, PDFGenerator generator) {
        this.historikk = historikk;
        this.oppslag = oppslag;
        this.minidialog = minidialog;
        this.journalføring = journalføring;
        this.generator = generator;
    }

    @KafkaListener(topics = "#{'${historikk.kafka.meldinger.topic}'}", groupId = "#{'${spring.kafka.consumer.group-id}'}")
    @Transactional
    public void listen(@Payload @Valid MinidialogInnslag innslag) {
        MDC.put(NAV_CALL_ID, innslag.getReferanseId());
        MDC.put(CALL_ID, innslag.getReferanseId());
        minidialog.lagre(innslag);
        String id = journalføring.sluttfør(
                journalpostFra(innslag, generator.generate("Overskrift TBD", innslag.getTekst())));
        historikk.lagre(innslag, id);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[minidialog=" + minidialog + ", oppslag=" + oppslag + ", journalføring="
                + journalføring + ", historikk=" + historikk + ", generator=" + generator + "]";
    }

}