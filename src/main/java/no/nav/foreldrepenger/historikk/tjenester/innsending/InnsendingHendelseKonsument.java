package no.nav.foreldrepenger.historikk.tjenester.innsending;

import static no.nav.foreldrepenger.common.util.Constants.NAV_CALL_ID;
import static no.nav.foreldrepenger.historikk.config.KafkaOnpremListenerConfiguration.CFONPREM;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import no.nav.foreldrepenger.common.util.MDCUtil;
import no.nav.foreldrepenger.historikk.tjenester.dittnav.DittNav;
import no.nav.foreldrepenger.historikk.tjenester.felles.HendelseType;
import no.nav.foreldrepenger.historikk.tjenester.tilbakekreving.Tilbakekreving;
import org.springframework.transaction.annotation.Transactional;

@Service
@ConditionalOnProperty(name = "historikk.innsending.søknad.enabled")
public class InnsendingHendelseKonsument {

    private static final Logger LOG = LoggerFactory.getLogger(InnsendingHendelseKonsument.class);

    private final Innsending innsending;
    private final Tilbakekreving tilbakekreving;
    private final DittNav dittNav;

    public InnsendingHendelseKonsument(Innsending innsending,
                                       Tilbakekreving tilbakekreving,
                                       DittNav dittNav) {
        this.innsending = innsending;
        this.tilbakekreving = tilbakekreving;
        this.dittNav = dittNav;
    }

    @Transactional
    @KafkaListener(topics = "#{'${historikk.innsending.søknad.topic}'}",
                   groupId = "#{'${historikk.innsending.søknad.group-id}'}",
                   containerFactory = CFONPREM)
    public void behandle(@Payload @Valid InnsendingHendelse h) {
        MDCUtil.toMDC(NAV_CALL_ID, h.getReferanseId());
        LOG.info("Mottok innsendingshendelse {}", h);
        innsending.lagreEllerOppdater(h);
        sjekkMangledeVedlegg(h);
        if (h.erEttersending() && (h.getDialogId() != null)) {
            LOG.info("Dette er en ettersending fra en tilbakekrevingsdialog med dialogId {}", h.getDialogId());
            avsluttOppgave(h);
        }
        dittNav.opprettBeskjed(h, "Vi mottok en " + h.getHendelse().beskrivelse);
    }

    private void avsluttOppgave(InnsendingHendelse h) {
        tilbakekreving.avsluttOppgave(h.getAktørId(), h.getDialogId());
        dittNav.avsluttOppgave(h.getDialogId());
    }

    private void sjekkMangledeVedlegg(InnsendingHendelse h) {
        if (h.getSaksnummer() == null) {
            LOG.info("Søknaden har intet saksnummer");
            return;
        }
        try {
            var info = innsending.vedleggsInfo(h.getFnr(), h.getSaksnummer(), h.getReferanseId());
            LOG.info("Vedleggsinfo {}", info);
            info.innsendte().forEach(dittNav::avsluttOppgave);
            if (info.manglerVedlegg()) {
                LOG.info("Det mangler vedlegg {} for sak {}", info.manglende(), h.getSaksnummer());
                dittNav.opprettOppgave(h,
                    String.format("Det mangler %s vedlegg i søknaden din om %s", info.manglende().size(),
                        sak(h.getHendelse())));
            } else {
                LOG.info("Det mangler ingen vedlegg for sak {}", h.getSaksnummer());

            }
        } catch (Exception e) {
            LOG.warn("Kunne ikke hente tidligere innsendinger", e);
        }
    }

    private static String sak(HendelseType hendelse) {
        if (hendelse.erEngangsstønad()) {
            return "engangsstønad";
        }
        if (hendelse.erForeldrepenger()) {
            return "foreldrepenger";
        }
        if (hendelse.erSvangerskapspenger()) {
            return "svangerskapspenger";
        }
        return "foreldrepenger";
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[innsending=" + innsending + ", dialog=" + tilbakekreving + ", dittNav="
            + dittNav + "]";
    }

}
