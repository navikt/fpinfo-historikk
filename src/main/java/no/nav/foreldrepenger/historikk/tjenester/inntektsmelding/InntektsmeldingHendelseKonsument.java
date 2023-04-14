package no.nav.foreldrepenger.historikk.tjenester.inntektsmelding;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static no.nav.foreldrepenger.common.util.Constants.NAV_CALL_ID;
import static no.nav.foreldrepenger.common.util.MDCUtil.toMDC;
import static no.nav.foreldrepenger.historikk.config.KafkaListenerConfiguration.AIVEN;

@Service
@ConditionalOnProperty(name = "historikk.inntektsmelding.enabled", havingValue = "true")
public class InntektsmeldingHendelseKonsument {

    private static final Logger LOG = LoggerFactory.getLogger(InntektsmeldingHendelseKonsument.class);

    private final Inntektsmelding inntektsmelding;

    public InntektsmeldingHendelseKonsument(Inntektsmelding inntektsmelding) {
        this.inntektsmelding = inntektsmelding;
    }

    @Transactional
    @KafkaListener(topics = "#{'${historikk.inntektsmelding.topic}'}",
                   groupId = "#{'${historikk.inntektsmelding.group-id}'}",
                   containerFactory = AIVEN)
    public void aivenKonsumer(@Payload @Valid InntektsmeldingHendelse h,
                              @Header(name = NAV_CALL_ID, required = false) String callId,
                              @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                              @Header(KafkaHeaders.RECEIVED_PARTITION) int partitionId,
                              @Header(KafkaHeaders.OFFSET) int offset) {
        toMDC(NAV_CALL_ID, callId);
        LOG.info("Mottok Inntektsmeldinghendelse med referanseId {} fra {}, partition {}, offset {}",
            h.getReferanseId(), topic, partitionId, offset);
        inntektsmelding.lagre(h);
        LOG.info("Lagret Inntektsmeldinghendelse med referanse {}", h.getReferanseId());
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[inntektsmelding=" + inntektsmelding + "]";
    }
}
