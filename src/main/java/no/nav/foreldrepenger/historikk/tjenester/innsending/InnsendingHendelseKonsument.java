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
import no.nav.foreldrepenger.historikk.tjenester.felles.UrlGenerator;
import no.nav.foreldrepenger.historikk.tjenester.tilbakekreving.Tilbakekreving;
import no.nav.foreldrepenger.historikk.util.MDCUtil;

@Service
@ConditionalOnProperty(name = "historikk.innsending.søknad.enabled")
public class InnsendingHendelseKonsument {

    private static final Logger LOG = LoggerFactory.getLogger(InnsendingHendelseKonsument.class);

    private final Innsending innsending;
    private final Tilbakekreving tilbakekreving;
    private final DittNav dittNav;
    private final UrlGenerator generator;

    public InnsendingHendelseKonsument(Innsending innsending, Tilbakekreving tilbakekreving,
            DittNav dittNav, UrlGenerator generator) {
        this.innsending = innsending;
        this.tilbakekreving = tilbakekreving;
        this.dittNav = dittNav;
        this.generator = generator;
    }

    @Transactional
    @KafkaListener(topics = "#{'${historikk.innsending.søknad.topic}'}", groupId = "#{'${spring.kafka.consumer.group-id}'}")
    public void behandle(@Payload @Valid InnsendingHendelse h) {
        MDCUtil.toMDC(NAV_CALL_ID, h.getReferanseId());
        LOG.info("Mottok innsendingshendelse {}", h);
        innsending.lagreEllerOppdater(h);
        sjekkMangledeVedlegg(h);
        if (h.erEttersending() && (h.getDialogId() != null)) {
            LOG.info("Dette er en ettersending fra en tilbakekrevingsdialog med dialogId {}", h.getDialogId());
            avsluttOppgave(h);
        }
        dittNav.opprettBeskjed(h.getFnr(), h.getSaksnummer(), h.getReferanseId(),
                "Vi mottok en " + h.getHendelse().beskrivelse,
                generator.url(h.getHendelse()));
    }

    private void avsluttOppgave(InnsendingHendelse h) {
        tilbakekreving.avsluttOppgave(h.getAktørId(), h.getDialogId());
        dittNav.avsluttOppgave(h.getFnr(), h.getSaksnummer(), h.getDialogId());
    }

    private void sjekkMangledeVedlegg(InnsendingHendelse h) {
        if (h.getSaksnummer() == null) {
            LOG.info("Søknaden ble rutet til GOSYS, intet saksnummer");
            return;
        }
        try {
            var info = innsending.vedleggsInfo(h.getFnr(), h.getSaksnummer(), h.getReferanseId());
            info.getRefs()
                    .stream()
                    .forEach(ref -> dittNav.avsluttOppgave(h.getFnr(), h.getSaksnummer(), ref));
            if (info.manglerVedlegg()) {
                dittNav.opprettOppgave(h.getFnr(), h.getSaksnummer(), h.getReferanseId(),
                        info.manglendeVedleggTekst(),
                        generator.url(h.getHendelse()), h.getSaksnummer());
            }
        } catch (Exception e) {
            LOG.warn("Kunne ikke hente tidligere innsendinger", e);
        }
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[innsending=" + innsending + ", dialog=" + tilbakekreving + ", dittNav="
                + dittNav + "]";
    }

}