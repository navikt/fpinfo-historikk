package no.nav.foreldrepenger.historikk.tjenester.innsending;

import static no.nav.foreldrepenger.historikk.config.Constants.NAV_CALL_ID;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import no.nav.foreldrepenger.historikk.tjenester.dittnav.DittNavOperasjoner;
import no.nav.foreldrepenger.historikk.tjenester.felles.UrlGenerator;
import no.nav.foreldrepenger.historikk.tjenester.tilbakekreving.TilbakekrevingTjeneste;
import no.nav.foreldrepenger.historikk.util.MDCUtil;

@Service
public class InnsendingHendelseKonsument {

    private static final Logger LOG = LoggerFactory.getLogger(InnsendingHendelseKonsument.class);

    private final InnsendingTjeneste innsending;
    private final TilbakekrevingTjeneste dialog;
    private final DittNavOperasjoner dittNav;
    private final UrlGenerator urlGenerator;

    public InnsendingHendelseKonsument(InnsendingTjeneste innsending, TilbakekrevingTjeneste dialog,
            DittNavOperasjoner dittNav, UrlGenerator urlGenerator) {
        this.innsending = innsending;
        this.dialog = dialog;
        this.dittNav = dittNav;
        this.urlGenerator = urlGenerator;
    }

    @Transactional
    @KafkaListener(topics = "#{'${historikk.kafka.topics.søknad}'}", groupId = "#{'${spring.kafka.consumer.group-id}'}")
    public void behandle(@Payload @Valid InnsendingHendelse h) {
        MDCUtil.toMDC(NAV_CALL_ID, h.getReferanseId());
        LOG.info("Mottok innsendingshendelse {}", h);
        innsending.lagreEllerOppdater(h);
        if (h.erEttersending() && (h.getDialogId() != null)) {
            LOG.info("Dette er en ettersending fra en minidialog");
            dialog.deaktiver(h.getAktørId(), h.getDialogId());
            dittNav.avsluttOppgave(h.getFnr(), h.getSaksnummer(), h.getDialogId());

        }
        if (!h.getIkkeOpplastedeVedlegg().isEmpty()) {
            // lag minidialoginnslag ?
        }
        dittNav.opprettBeskjed(h.getFnr(), h.getSaksnummer(), "Mottatt " + h.getHendelse().beskrivelse,
                urlGenerator.url(h.getHendelse()), h.getSaksnummer());

    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[innsending=" + innsending + ", dialog=" + dialog + ", dittNav="
                + dittNav + "]";
    }

}