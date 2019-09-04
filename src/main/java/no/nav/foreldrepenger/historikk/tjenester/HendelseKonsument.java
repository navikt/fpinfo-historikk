package no.nav.foreldrepenger.historikk.tjenester;

import static no.nav.foreldrepenger.historikk.config.Constants.NAV_CALL_ID;

import javax.validation.Valid;

import org.jboss.logging.MDC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import no.nav.foreldrepenger.historikk.tjenester.inntektsmelding.InntektsmeldingHendelse;
import no.nav.foreldrepenger.historikk.tjenester.minidialog.MinidialogTjeneste;
import no.nav.foreldrepenger.historikk.tjenester.søknad.SøknadInnsendingHendelse;
import no.nav.foreldrepenger.historikk.tjenester.søknad.SøknadsHistorikkTjeneste;

@Service
public class HendelseKonsument {

    private static final Logger LOG = LoggerFactory.getLogger(HendelseKonsument.class);

    private final SøknadsHistorikkTjeneste historikk;
    private final MinidialogTjeneste dialog;

    public HendelseKonsument(SøknadsHistorikkTjeneste historikk, MinidialogTjeneste dialog) {
        this.historikk = historikk;
        this.dialog = dialog;
    }

    @Transactional
    @KafkaListener(topics = "#{'${historikk.kafka.meldinger.søknad_topic}'}", groupId = "#{'${spring.kafka.consumer.group-id}'}")
    public void behandleSøknad(@Payload @Valid SøknadInnsendingHendelse hendelse) {
        LOG.info("Mottok hendelse om søknad {}", hendelse);
        MDC.put(NAV_CALL_ID, hendelse.getReferanseId());
        historikk.lagre(hendelse);
        dialog.deaktiverMinidialoger(hendelse.getFnr(), hendelse.getHendelse(), hendelse.getSaksNr());
    }

    @Transactional
    @KafkaListener(topics = "#{'${historikk.kafka.meldinger.inntektsmelding_topic}'}", groupId = "#{'${spring.kafka.consumer.group-id}'}")
    public void behandleInnteksmelding(@Payload @Valid InntektsmeldingHendelse hendelse) {
        LOG.info("Mottok hendelse om inntektsmelding {}", hendelse);
        MDC.put(NAV_CALL_ID, hendelse.getReferanseId());
        historikk.lagre(hendelse);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[historikk=" + historikk + ", dialog=" + dialog + "]";
    }
}