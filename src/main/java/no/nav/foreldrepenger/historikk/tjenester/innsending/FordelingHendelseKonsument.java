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
import no.nav.foreldrepenger.historikk.tjenester.felles.HendelseType;
import no.nav.foreldrepenger.historikk.util.MDCUtil;

@Service
@ConditionalOnProperty(name = "historikk.innsending.fordeling.enabled")
public class FordelingHendelseKonsument {

    private static final Logger LOG = LoggerFactory.getLogger(FordelingHendelseKonsument.class);

    private final Innsending innsending;
    private final DittNav dittNav;

    public FordelingHendelseKonsument(Innsending innsending, DittNav dittNav) {
        this.innsending = innsending;
        this.dittNav = dittNav;
    }

    @Transactional
    @KafkaListener(topics = "#{'${historikk.innsending.fordeling.topic}'}", groupId = "#{'${historikk.innsending.fordeling.group-id}'}")
    public void behandle(@Payload @Valid InnsendingFordeltOgJournalførtHendelse h) {
        MDCUtil.toMDC(NAV_CALL_ID, h.getForsendelseId());
        LOG.info("Mottok fordelingshendelse {}", h);
        innsending.lagreEllerOppdater(h);
        sjekkMangledeVedlegg(h);
    }

    private void sjekkMangledeVedlegg(InnsendingFordeltOgJournalførtHendelse h) {
        if (h.getSaksnummer() == null) {
            LOG.info("Søknaden har intet saksnummer");
            return;
        }
        try {
            var info = innsending.vedleggsInfo(h.getFnr(), h.getSaksnummer(), h.getForsendelseId());
            LOG.info("Vedleggsinfo {}", info);
            info.getInnsendte()
                    .stream()
                    .forEach(ref -> dittNav.avsluttOppgave(h.getFnr(), h.getSaksnummer(), ref));
            if (info.manglerVedlegg()) {
                LOG.info("Det mangler vedlegg {} for sak {}", info.getManglende(), h.getSaksnummer());
                dittNav.opprettOppgave(h,
                        String.format("Det mangler %s vedlegg i søknaden din om %s", info.getManglende().size(), sak(h.getHendelse())));
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
        return getClass().getSimpleName() + "[innsending=" + innsending + "]";
    }
}