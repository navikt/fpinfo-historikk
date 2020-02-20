package no.nav.foreldrepenger.historikk.tjenester.minidialog;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import no.nav.foreldrepenger.historikk.tjenester.dittnav.DittNavOperasjoner;

@Service
public class MinidialogHendelseKonsument {

    private static final Logger LOG = LoggerFactory.getLogger(MinidialogHendelseKonsument.class);
    private final MinidialogTjeneste dialog;
    private final DittNavOperasjoner dittNav;

    public MinidialogHendelseKonsument(MinidialogTjeneste dialog, DittNavOperasjoner dittNav) {
        this.dialog = dialog;
        this.dittNav = dittNav;
    }

    @KafkaListener(topics = "#{'${historikk.kafka.topics.tilbakekreving}'}", groupId = "#{'${spring.kafka.consumer.group-id}'}")
    @Transactional
    public void listen(@Payload @Valid MinidialogHendelse hendelse) {
        LOG.info("Mottok minidialoghendelse {}", hendelse);
        switch (hendelse.getHendelse()) {
        case TILBAKEKREVING_SPM:
            dialog.lagre(hendelse);
            dittNav.opprettOppgave(hendelse);
            break;
        case TILBAKEKREVING_SVAR:
            dialog.deaktiver(hendelse.getAktørId(), hendelse.getDialogId());
            dittNav.avsluttOppgave(hendelse);
            break;
        default:
            LOG.warn("Hendelsetype {} ikke støttet", hendelse.getHendelse());
            break;
        }
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[dialog=" + dialog + "]";
    }
}