package no.nav.foreldrepenger.historikk.tjenester.minidialog;

import static no.nav.foreldrepenger.historikk.tjenester.felles.HendelseType.TILBAKEKREVING_SPM;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import no.nav.foreldrepenger.historikk.tjenester.felles.HendelseType;

@Service
public class MinidialogHendelseKonsument {

    private static final Logger LOG = LoggerFactory.getLogger(MinidialogHendelseKonsument.class);
    private final MinidialogTjeneste dialog;

    public MinidialogHendelseKonsument(MinidialogTjeneste dialog) {
        this.dialog = dialog;
    }

    @KafkaListener(topics = "#{'${historikk.kafka.meldinger.topic}'}", groupId = "#{'${spring.kafka.consumer.group-id}'}")
    @Transactional
    public void listen(@Payload @Valid MinidialogHendelse hendelse) {
        LOG.info("Mottok hendelse om minidialog {}", hendelse);
        dialog.lagre(hendelse);
        // TODO, journalføre om det er et spørsmål ?
    }

    private boolean skalJournalføre(HendelseType type) {
        return TILBAKEKREVING_SPM.equals(type);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[dialog=" + dialog + "]";
    }
}