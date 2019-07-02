package no.nav.foreldrepenger.historikk.tjenester.minidialog;

import static no.nav.foreldrepenger.historikk.config.Constants.CALL_ID;
import static no.nav.foreldrepenger.historikk.config.Constants.NAV_CALL_ID;
import static no.nav.foreldrepenger.historikk.tjenester.minidialog.MinidialogMapper.journalpostFra;
import static no.nav.foreldrepenger.historikk.util.EnvUtil.DEV;
import static no.nav.foreldrepenger.historikk.util.EnvUtil.PREPROD;

import javax.validation.Valid;

import org.jboss.logging.MDC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import no.nav.foreldrepenger.historikk.tjenester.historikk.HistorikkTjeneste;
import no.nav.foreldrepenger.historikk.tjenester.journalføring.JournalføringTjeneste;
import no.nav.foreldrepenger.historikk.tjenester.journalføring.pdf.PDFGenerator;

@Service
@Profile({ DEV, PREPROD })
public class MinidialogEventKonsument {

    private static final Logger LOG = LoggerFactory.getLogger(MinidialogEventKonsument.class);
    private final MinidialogTjeneste dialog;
    private final JournalføringTjeneste journalføring;
    private final HistorikkTjeneste historikk;
    private final PDFGenerator generator;

    public MinidialogEventKonsument(HistorikkTjeneste historikk, MinidialogTjeneste minidialog,
            JournalføringTjeneste journalføring, PDFGenerator generator) {
        this.historikk = historikk;
        this.dialog = minidialog;
        this.journalføring = journalføring;
        this.generator = generator;
    }

    @KafkaListener(topics = "#{'${historikk.kafka.meldinger.topic}'}", groupId = "#{'${spring.kafka.consumer.group-id}'}")
    @Transactional
    public void listen(@Payload @Valid MinidialogInnslag innslag) {
        LOG.info("Mottok innslag {}", innslag);
        MDC.put(NAV_CALL_ID, innslag.getReferanseId());
        MDC.put(CALL_ID, innslag.getReferanseId());
        dialog.lagre(innslag);
        String id = journalføring.sluttfør(
                journalpostFra(innslag, generator.generate("Spørsmål fra saksbehandler", innslag.getTekst())));
        historikk.lagre(innslag, id);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[minidialog=" + dialog + ", journalføring="
                + journalføring + ", historikk=" + historikk + ", generator=" + generator + "]";
    }

}