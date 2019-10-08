package no.nav.foreldrepenger.historikk.tjenester.innsending;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import no.nav.foreldrepenger.historikk.domain.Fødselsnummer;
import no.nav.foreldrepenger.historikk.tjenester.minidialog.MinidialogTjeneste;
import no.nav.foreldrepenger.historikk.tjenester.oppslag.OppslagTjeneste;

@Service
public class InnsendingHendelseKonsument {

    private static final Logger LOG = LoggerFactory.getLogger(InnsendingHendelseKonsument.class);

    private final InnsendingTjeneste innsending;
    private final MinidialogTjeneste dialog;
    private final OppslagTjeneste oppslag;

    public InnsendingHendelseKonsument(InnsendingTjeneste innsending, MinidialogTjeneste dialog,
            OppslagTjeneste oppslag) {
        this.innsending = innsending;
        this.dialog = dialog;
        this.oppslag = oppslag;
    }

    @Transactional
    @KafkaListener(topics = "#{'${historikk.kafka.meldinger.søknad_topic}'}", groupId = "#{'${spring.kafka.consumer.group-id}'}")
    public void behandle(@Payload @Valid InnsendingHendelse hendelse) {
        LOG.info("Mottok hendelse om innsending {}", hendelse);
        Fødselsnummer fnr = oppslag.fnr(hendelse.getAktørId());
        innsending.lagre(hendelse, fnr);
        dialog.deaktiver(hendelse, fnr);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[innsending=" + innsending + ", dialog=" + dialog + "]";
    }
}