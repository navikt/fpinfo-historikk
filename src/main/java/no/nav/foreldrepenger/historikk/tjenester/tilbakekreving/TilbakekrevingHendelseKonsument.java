package no.nav.foreldrepenger.historikk.tjenester.tilbakekreving;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import no.nav.foreldrepenger.historikk.tjenester.dittnav.DittNavOperasjoner;

@Service
public class TilbakekrevingHendelseKonsument {

    private static final Logger LOG = LoggerFactory.getLogger(TilbakekrevingHendelseKonsument.class);
    private final TilbakekrevingTjeneste dialog;
    private final DittNavOperasjoner dittNav;

    public TilbakekrevingHendelseKonsument(TilbakekrevingTjeneste dialog, DittNavOperasjoner dittNav) {
        this.dialog = dialog;
        this.dittNav = dittNav;
    }

    @KafkaListener(topics = "#{'${historikk.kafka.topics.tilbakekreving}'}", groupId = "#{'${spring.kafka.consumer.group-id}'}")
    @Transactional
    public void listen(@Payload @Valid TilbakekrevingHendelse h) {
        LOG.info("Mottok tilbakekrevingshendelse {}", h);
        switch (h.getHendelse()) {
        case TILBAKEKREVING_SPM:
            dialog.opprettOppgave(h);
            dittNav.opprettOppgave(h.getFnr(), h.getSaksnummer(), "Tilbakekrevingssak",
                    h.getHendelse(), h.getDialogId());
            break;
        default:
            LOG.warn("Hendelsetype {} ikke støttet", h.getHendelse());
            break;
        }
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[dialog=" + dialog + ", dittNav=" + dittNav + "]";
    }

}