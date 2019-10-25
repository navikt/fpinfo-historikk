package no.nav.foreldrepenger.historikk.tjenester.inntektsmelding;

import static no.nav.foreldrepenger.historikk.config.Constants.NAV_CALL_ID;

import javax.validation.Valid;

import org.jboss.logging.MDC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@ConditionalOnProperty(name = "historikk.inntektsmelding.enabled", havingValue = "true")
public class InntektsmeldingHendelseKonsument {

    private static final Logger LOG = LoggerFactory.getLogger(InntektsmeldingHendelseKonsument.class);

    private final InntektsmeldingTjeneste inntektsmelding;

    public InntektsmeldingHendelseKonsument(InntektsmeldingTjeneste inntektsmelding) {
        this.inntektsmelding = inntektsmelding;
    }

    @Transactional
    @KafkaListener(topics = "#{'${historikk.kafka.meldinger.inntektsmelding_topic}'}", groupId = "#{'${spring.kafka.consumer.group-id}'}")
    public void konsumer(@Payload @Valid InntektsmeldingHendelse hendelse,
            @Header(name = NAV_CALL_ID, required = false) String callId) {
        if (callId != null) {
            MDC.put(NAV_CALL_ID, callId);
        }
        LOG.info("Mottok inntektsmeldinghendelse {}", hendelse);
        inntektsmelding.lagre(hendelse);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[inntektsmelding=" + inntektsmelding + "]";
    }
}