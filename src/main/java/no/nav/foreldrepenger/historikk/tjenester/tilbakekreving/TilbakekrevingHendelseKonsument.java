package no.nav.foreldrepenger.historikk.tjenester.tilbakekreving;

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
import no.nav.foreldrepenger.historikk.util.MDCUtil;

@Service
@ConditionalOnProperty(name = "historikk.tilbakekreving.enabled")
public class TilbakekrevingHendelseKonsument {

    private static final Logger LOG = LoggerFactory.getLogger(TilbakekrevingHendelseKonsument.class);
    private final Tilbakekreving tilbakekreving;
    private final DittNav dittNav;

    public TilbakekrevingHendelseKonsument(Tilbakekreving tilbakekreving, DittNav dittNav) {
        this.tilbakekreving = tilbakekreving;
        this.dittNav = dittNav;
    }

    @KafkaListener(topics = "#{'${historikk.tilbakekreving.topic}'}", groupId = "#{'${kafka.onprem.consumer-group-id}'}")
    @Transactional
    public void listen(@Payload @Valid TilbakekrevingHendelse h) {
        MDCUtil.toMDC(NAV_CALL_ID, h.getDialogId());
        LOG.info("Mottok tilbakekrevingshendelse {}", h);
        switch (h.getHendelse()) {
            case TILBAKEKREVING_SPM -> opprettOppgave(h);
            case TILBAKEKREVING_FATTET_VEDTAK, TILBAKEKREVING_SPM_LUKKET, TILBAKEKREVING_HENLAGT -> avsluttOppgave(h);
            default -> LOG.warn("Hendelsetype {} ikke støttet", h.getHendelse());
        }
    }

    private void avsluttOppgave(TilbakekrevingHendelse h) {
        LOG.info("Avslutter oppgave i selvbetjening og Ditt Nav grunnet hendelse {}", h);
        tilbakekreving.avsluttOppgave(h);
        // dittNav.avsluttOppgave(h.getFnr(), h.getSaksnummer(), h.getDialogId());
    }

    private void opprettOppgave(TilbakekrevingHendelse h) {
        LOG.info("Oppretter oppgave i selvbetjening og Ditt Nav grunnet hendelse {}", h);
        tilbakekreving.opprettOppgave(h);
        // dittNav.opprettOppgave(h.getFnr(), h.getSaksnummer(), h.getDialogId(),
        // "Tilbakekrevingssak", h.getHendelse());
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[tilbakekreving=" + tilbakekreving + ", dittNav=" + dittNav + "]";
    }

}
