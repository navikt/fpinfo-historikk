package no.nav.foreldrepenger.historikk.tjenester.innsending;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import no.nav.foreldrepenger.historikk.tjenester.minidialog.MinidialogTjeneste;

@Service
public class InnsendingHendelseKonsument {

    private static final Logger LOG = LoggerFactory.getLogger(InnsendingHendelseKonsument.class);

    private final InnsendingTjeneste innsending;
    private final MinidialogTjeneste dialog;

    public InnsendingHendelseKonsument(InnsendingTjeneste innsending, MinidialogTjeneste dialog) {
        this.innsending = innsending;
        this.dialog = dialog;
    }

    @Transactional
    @KafkaListener(topics = "#{'${historikk.kafka.meldinger.søknad_topic}'}", groupId = "#{'${spring.kafka.consumer.group-id}'}")
    public void behandle(@Payload @Valid InnsendingHendelse hendelse) {
        LOG.info("Mottok hendelse om innsending {}", hendelse);
        innsending.lagre(hendelse);
        dialog.deaktiver(hendelse.getAktørId(), hendelse.getReferanseId());
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[innsending=" + innsending + ", dialog=" + dialog + "]";
    }
}