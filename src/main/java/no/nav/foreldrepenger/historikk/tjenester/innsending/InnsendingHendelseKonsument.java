package no.nav.foreldrepenger.historikk.tjenester.innsending;

import static no.nav.foreldrepenger.historikk.config.Constants.NAV_CALL_ID;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import no.nav.foreldrepenger.historikk.tjenester.dittnav.DittNav;
import no.nav.foreldrepenger.historikk.tjenester.tilbakekreving.Tilbakekreving;
import no.nav.foreldrepenger.historikk.util.MDCUtil;

@Service
@ConditionalOnProperty(name = "historikk.innsending.søknad.enabled")
public class InnsendingHendelseKonsument {

    private static final Logger LOG = LoggerFactory.getLogger(InnsendingHendelseKonsument.class);

    private final Innsending innsending;
    private final Tilbakekreving tilbakekreving;
    private final DittNav dittNav;

    public InnsendingHendelseKonsument(Innsending innsending, Tilbakekreving tilbakekreving,
            DittNav dittNav) {
        this.innsending = innsending;
        this.tilbakekreving = tilbakekreving;
        this.dittNav = dittNav;
    }

    @Transactional
    @KafkaListener(topics = "#{'${historikk.innsending.søknad.topic}'}", groupId = "#{'${spring.kafka.consumer.group-id}'}")
    public void behandle(@Payload @Valid InnsendingHendelse h) {
        MDCUtil.toMDC(NAV_CALL_ID, h.getReferanseId());
        LOG.info("Mottok innsendingshendelse {}", h);
        innsending.lagreEllerOppdater(h);
        if (h.erEttersending() && (h.getDialogId() != null)) {
            LOG.info("Dette er en ettersending fra en tilbakekrevingsdialog med dialogId {}", h.getDialogId());
            tilbakekreving.avsluttDialog(h.getAktørId(), h.getDialogId());
            // LOG.info("Avslutter oppgave i Ditt Nav etter {}", h);
            // dittNav.avsluttOppgave(h.getFnr(), h.getSaksnummer(), h.getDialogId());
        }
        if (!h.getIkkeOpplastedeVedlegg().isEmpty()) {
            // lag minidialoginnslag ?
        }

        dittNav.opprettBeskjed(h.getFnr(), h.getSaksnummer(), h.getReferanseId(), "Mottatt ", h.getHendelse());
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[innsending=" + innsending + ", dialog=" + tilbakekreving + ", dittNav="
                + dittNav + "]";
    }

}