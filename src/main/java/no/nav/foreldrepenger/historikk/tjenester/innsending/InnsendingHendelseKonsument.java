package no.nav.foreldrepenger.historikk.tjenester.innsending;

import static no.nav.foreldrepenger.historikk.config.Constants.NAV_CALL_ID;

import javax.validation.Valid;

import org.jboss.logging.MDC;
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
    public void behandleSøknad(@Payload @Valid InnsendingHendelse hendelse) {
        LOG.info("Mottok innsendingshendelse {}", hendelse);
        MDC.put(NAV_CALL_ID, hendelse.getReferanseId());
        innsending.lagre(hendelse);
        dialog.deaktiverMinidialoger(hendelse);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[innsending=" + innsending + ", dialog=" + dialog + "]";
    }
}