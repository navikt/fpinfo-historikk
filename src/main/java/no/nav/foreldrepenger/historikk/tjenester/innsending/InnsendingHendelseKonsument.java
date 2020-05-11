package no.nav.foreldrepenger.historikk.tjenester.innsending;

import static no.nav.foreldrepenger.historikk.config.Constants.NAV_CALL_ID;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        sjekkManglede(h);
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

    private void logVedlegg(InnsendingHendelse h) {
        if (!h.getOpplastedeVedlegg().isEmpty()) {
            LOG.info("({}) Følgende vedlegg er  lastet opp {}", h.getHendelse(), h.getOpplastedeVedlegg());
        }
        if (!h.getIkkeOpplastedeVedlegg().isEmpty()) {
            LOG.info("({}) Følgende vedlegg er IKKE lastet opp {}", h.getHendelse(), h.getIkkeOpplastedeVedlegg());
        }
    }

    private void sjekkManglede(InnsendingHendelse h) {
        try {
            var manglende = new ArrayList<String>();
            var refs = new ArrayList<String>();
            logVedlegg(h);
            String saksnummer = Optional.ofNullable(h.getSaksnummer()).orElse(h.getReferanseId());
            for (var tidligere : innsending.finnForSaksnr(saksnummer)) {
                LOG.trace("Tidligere innsending for {} er {} ", saksnummer, tidligere);
                if (tidligere.getReferanseId() != h.getReferanseId()) {
                    refs.add(tidligere.getReferanseId());
                }
                if (tidligere.getHendelse().erInitiell()) {
                    manglende.clear();
                }
                LOG.trace("Legger til {} i {}", tidligere.ikkeOpplastedeVedlegg(), manglende);
                manglende.addAll(tidligere.ikkeOpplastedeVedlegg());
                LOG.trace("Fjerner {} fra {}", tidligere.opplastedeVedlegg(), manglende);
                tidligere.opplastedeVedlegg().stream().forEach(manglende::remove);
                LOG.trace("Ikke-opplastede etter fjerning er {}", manglende);
            }
            refs.stream().forEach(r -> dittNav.avsluttOppgave(h.getFnr(), saksnummer, r));
            LOG.info("Ikke-opplastede vedlegg for {} {}", saksnummer, manglende);
            if (!manglende.isEmpty()) {
                dittNav.opprettOppgave(h.getFnr(), saksnummer, h.getReferanseId(), manglendeVedlegg(manglende),
                        generator.url(h.getHendelse()));
            }
        } catch (Exception e) {
            LOG.warn("Kunne ikke hente tidligere innsendinger", e);
        }
    }

    private String manglendeVedlegg(List<String> ids) {
        return "Vi mangler følgende " + ids.size() + " vedlegg: " + beskrivelseFor(ids);
    }

    private String beskrivelseFor(List<String> ids) {
        return ids.stream()
                .map(this::beskrivelseFor)
                .collect(Collectors.joining(", "));
    }

    private String beskrivelseFor(String id) {
        return DokumentType.valueOf(id).getBeskrivelse();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[innsending=" + innsending + ", dialog=" + tilbakekreving + ", dittNav="
                + dittNav + "]";
    }

}