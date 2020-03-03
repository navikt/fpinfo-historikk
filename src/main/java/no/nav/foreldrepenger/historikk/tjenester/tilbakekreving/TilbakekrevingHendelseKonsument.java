package no.nav.foreldrepenger.historikk.tjenester.tilbakekreving;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import no.nav.foreldrepenger.historikk.tjenester.dittnav.DittNavOperasjoner;
import no.nav.foreldrepenger.historikk.tjenester.felles.UrlGenerator;

@Service
public class TilbakekrevingHendelseKonsument {

    private static final Logger LOG = LoggerFactory.getLogger(TilbakekrevingHendelseKonsument.class);
    private final TilbakekrevingTjeneste dialog;
    private final DittNavOperasjoner dittNav;
    private final UrlGenerator urlGenerator;

    public TilbakekrevingHendelseKonsument(TilbakekrevingTjeneste dialog, DittNavOperasjoner dittNav,
            UrlGenerator urlGenerator) {
        this.dialog = dialog;
        this.dittNav = dittNav;
        this.urlGenerator = urlGenerator;
    }

    @KafkaListener(topics = "#{'${historikk.kafka.topics.tilbakekreving}'}", groupId = "#{'${spring.kafka.consumer.group-id}'}")
    @Transactional
    public void listen(@Payload @Valid TilbakekrevingHendelse h) {
        LOG.info("Mottok tilbakekrevingshendelse {}", h);
        switch (h.getHendelse()) {
        case TILBAKEKREVING_SPM:
            dialog.opprettOppgave(h);
            dittNav.opprettOppgave(h.getFnr(), h.getSaksnummer(), "Tilbakekrevingssak",
                    urlGenerator.url(h.getHendelse()), h.getDialogId());
            break;
        default:
            LOG.warn("Hendelsetype {} ikke støttet", h.getHendelse());
            break;
        }
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[dialog=" + dialog + ", dittNav=" + dittNav + ", urlGenerator="
                + urlGenerator + "]";
    }

}