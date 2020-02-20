package no.nav.foreldrepenger.historikk.tjenester.inntektsmelding;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaOperations;
import org.springframework.stereotype.Service;

import no.nav.foreldrepenger.historikk.tjenester.felles.AbstractHendelseProdusent;
import no.nav.foreldrepenger.historikk.util.ObjectMapperWrapper;

@Service
public class InntektsmeldingHendelseProdusent extends AbstractHendelseProdusent<InntektsmeldingHendelse> {

    public InntektsmeldingHendelseProdusent(KafkaOperations<String, String> kafkaOperations,
            @Value("${historikk.kafka.topics.inntektsmelding}") String topic,
            ObjectMapperWrapper mapper) {
        super(topic, kafkaOperations, mapper);
    }
}
