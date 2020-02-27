package no.nav.foreldrepenger.historikk.tjenester.minidialog;

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
public class MinidialogHendelseKonsument {

    private static final Logger LOG = LoggerFactory.getLogger(MinidialogHendelseKonsument.class);
    private final MinidialogTjeneste dialog;
    private final DittNavOperasjoner dittNav;
    private final UrlGenerator urlGenerator;

    public MinidialogHendelseKonsument(MinidialogTjeneste dialog, DittNavOperasjoner dittNav,
            UrlGenerator urlGenerator) {
        this.dialog = dialog;
        this.dittNav = dittNav;
        this.urlGenerator = urlGenerator;
    }

    @KafkaListener(topics = "#{'${historikk.kafka.topics.tilbakekreving}'}", groupId = "#{'${spring.kafka.consumer.group-id}'}")
    @Transactional
    public void listen(@Payload @Valid MinidialogHendelse h) {
        LOG.info("Mottok minidialoghendelse {}", h);
        switch (h.getHendelse()) {
        case TILBAKEKREVING_SPM:
            dialog.lagre(h);
            // dittNav.opprettOppgave(h.getFnr(), h.getSaksnummer(), "TODO",
            // urlGenerator.url(h.getHendelse()),
            // h.getDialogId());
            break;
        case TILBAKEKREVING_SVAR:
            // dialog.deaktiver(h.getAktørId(), h.getDialogId());
            // dittNav.avsluttOppgave(h.getFnr(), h.getSaksnummer(), h.getDialogId());
            break;
        default:
            LOG.warn("Hendelsetype {} ikke støttet", h.getHendelse());
            break;
        }
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[dialog=" + dialog + "]";
    }

}