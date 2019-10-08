package no.nav.foreldrepenger.historikk.tjenester.minidialog;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MinidialogHendelseKonsument {

    private static final Logger LOG = LoggerFactory.getLogger(MinidialogHendelseKonsument.class);
    private final MinidialogTjeneste dialog;

    public MinidialogHendelseKonsument(MinidialogTjeneste dialog) {
        this.dialog = dialog;
    }

    @KafkaListener(topics = "#{'${historikk.kafka.meldinger.topic}'}", groupId = "#{'${spring.kafka.consumer.group-id}'}")
    @Transactional
    public void listen(@Payload @Valid MinidialogHendelse h) {
        LOG.info("Mottok minidialoghendelse {}", h);
        switch (h.getHendelseType()) {
        case TILBAKEKREVING_SPM:
            dialog.lagre(h);
            break;
        case TILBAKEKREVING_SVAR:
            dialog.deaktiver(h.getAktørId(), h.getReferanseId());
            break;
        default:
            LOG.warn("Hendelsetype {} ikke støttet", h.getHendelseType());
            break;
        }
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[dialog=" + dialog + "]";
    }
}