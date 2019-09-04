package no.nav.foreldrepenger.historikk.tjenester.inntektsmelding;

import static no.nav.foreldrepenger.historikk.config.Constants.NAV_CALL_ID;

import javax.validation.Valid;

import org.jboss.logging.MDC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@ConditionalOnProperty(name = "historikk.inntektsmelding.enabled", havingValue = "true")
public class InntektsmeldingHendelseKonsument {

    private static final Logger LOG = LoggerFactory.getLogger(InntektsmeldingHendelseKonsument.class);

    private final InntektsmeldingHistorikkTjeneste historikk;

    public InntektsmeldingHendelseKonsument(InntektsmeldingHistorikkTjeneste historikk) {
        this.historikk = historikk;
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
        return getClass().getSimpleName() + "[historikk=" + historikk + "]";
    }
}