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

import no.nav.foreldrepenger.historikk.tjenester.historikk.HistorikkTjeneste;
import no.nav.foreldrepenger.historikk.tjenester.minidialog.MinidialogTjeneste;

@Service
public class InnsendingHendelseKonsument {

    private static final Logger LOG = LoggerFactory.getLogger(InnsendingHendelseKonsument.class);

    private final HistorikkTjeneste historikk;
    private final MinidialogTjeneste dialog;

    public InnsendingHendelseKonsument(HistorikkTjeneste historikk, MinidialogTjeneste dialog) {
        this.historikk = historikk;
        this.dialog = dialog;
    }

    @Transactional
    @KafkaListener(topics = "#{'${historikk.kafka.meldinger.søknad_topic}'}", groupId = "#{'${spring.kafka.consumer.group-id}'}")
    public void listen(@Payload @Valid SøknadInnsendingHendelse hendelse) {
        LOG.info("Mottok event {}", hendelse);
        MDC.put(NAV_CALL_ID, hendelse.getReferanseId());
        historikk.lagre(hendelse);
        dialog.deaktiverMinidialoger(hendelse.getFnr(), hendelse.getHendelse(), hendelse.getSaksNr());
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[historikk=" + historikk + ", dialog=" + dialog + "]";
    }
}