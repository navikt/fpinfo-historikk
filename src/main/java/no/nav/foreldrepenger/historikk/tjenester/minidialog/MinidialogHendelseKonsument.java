package no.nav.foreldrepenger.historikk.tjenester.minidialog;

import static no.nav.foreldrepenger.historikk.config.Constants.CALL_ID;
import static no.nav.foreldrepenger.historikk.config.Constants.NAV_CALL_ID;
import static no.nav.foreldrepenger.historikk.tjenester.minidialog.MinidialogMapper.journalpostFra;
import static no.nav.foreldrepenger.historikk.util.EnvUtil.DEV;
import static no.nav.foreldrepenger.historikk.util.EnvUtil.LOCAL;

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
@Profile({ LOCAL, DEV })
public class MinidialogHendelseKonsument {

    private static final Logger LOG = LoggerFactory.getLogger(MinidialogHendelseKonsument.class);
    private final MinidialogTjeneste dialog;
    private final JournalføringTjeneste journalføring;
    private final HistorikkTjeneste historikk;
    private final PDFGenerator generator;

    public MinidialogHendelseKonsument(HistorikkTjeneste historikk, MinidialogTjeneste minidialog,
            JournalføringTjeneste journalføring, PDFGenerator generator) {
        this.historikk = historikk;
        this.dialog = minidialog;
        this.journalføring = journalføring;
        this.generator = generator;
    }

    @KafkaListener(topics = "#{'${historikk.kafka.meldinger.topic}'}", groupId = "#{'${spring.kafka.consumer.group-id}'}")
    @Transactional
    public void listen(@Payload @Valid MinidialogHendelse hendelse) {
        LOG.info("Mottok innslag {}", hendelse);
        MDC.put(NAV_CALL_ID, hendelse.getReferanseId());
        MDC.put(CALL_ID, hendelse.getReferanseId());
        dialog.lagre(hendelse);
        String id = journalføring.sluttfør(
                journalpostFra(hendelse, generator.generate("Spørsmål fra saksbehandler", hendelse.getTekst())));
        historikk.lagre(hendelse, id);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[minidialog=" + dialog + ", journalføring="
                + journalføring + ", historikk=" + historikk + ", generator=" + generator + "]";
    }

}