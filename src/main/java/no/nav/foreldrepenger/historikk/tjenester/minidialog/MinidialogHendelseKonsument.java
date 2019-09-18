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

import no.nav.foreldrepenger.historikk.errorhandling.UnexpectedResponseException;
import no.nav.foreldrepenger.historikk.tjenester.felles.HendelseType;
import no.nav.foreldrepenger.historikk.tjenester.journalføring.Journalføring;
import no.nav.foreldrepenger.historikk.tjenester.journalføring.pdf.PDFGenerator;

@Service
@Profile({ LOCAL, DEV })
public class MinidialogHendelseKonsument {

    private static final Logger LOG = LoggerFactory.getLogger(MinidialogHendelseKonsument.class);
    private final MinidialogTjeneste dialog;
    private final Journalføring journalføring;
    private final PDFGenerator pdfGenerator;

    public MinidialogHendelseKonsument(MinidialogTjeneste minidialog, Journalføring journalføring,
            PDFGenerator pdfGenerator) {
        this.dialog = minidialog;
        this.journalføring = journalføring;
        this.pdfGenerator = pdfGenerator;
    }

    @KafkaListener(topics = "#{'${historikk.kafka.meldinger.topic}'}", groupId = "#{'${spring.kafka.consumer.group-id}'}")
    @Transactional
    public void listen(@Payload @Valid MinidialogHendelse hendelse) {
        LOG.info("Mottok hendelse {}", hendelse);
        MDC.put(NAV_CALL_ID, hendelse.getReferanseId());
        MDC.put(CALL_ID, hendelse.getReferanseId());
        byte[] dokument = pdfGenerator.generate(header(hendelse.getHendelse()), hendelse.getTekst());
        String journalPostId = journalføring.sluttfør(journalpostFra(hendelse, dokument));
        dialog.lagre(hendelse, journalPostId);
    }

    private static String header(HendelseType hendelseType) {
        switch (hendelseType) {
        case TILBAKEKREVING_SPM:
            return "Spørsmål fra saksbehandler";
        case TILBAKEKREVING_SVAR:
            return "Svar fra bruker";
        default:
            throw new UnexpectedResponseException("Uventet hendelsestype " + hendelseType);
        }

    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[minidialog=" + dialog + ", journalføring=" + journalføring
                + ", generator=" + pdfGenerator + "]";
    }
}