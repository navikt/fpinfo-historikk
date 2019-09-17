package no.nav.foreldrepenger.historikk.tjenester.inntektsmelding;

import static no.nav.foreldrepenger.historikk.util.EnvUtil.DEV;
import static no.nav.foreldrepenger.historikk.util.EnvUtil.LOCAL;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.KafkaOperations;
import org.springframework.stereotype.Service;

import no.nav.foreldrepenger.historikk.tjenester.felles.AbstractHendelseProdusent;
import no.nav.foreldrepenger.historikk.util.ObjectMapperWrapper;

@Service
@Profile({ LOCAL, DEV })
public class InntektsmeldingHendelseProdusent extends AbstractHendelseProdusent {

    public InntektsmeldingHendelseProdusent(KafkaOperations<String, String> kafkaOperations,
            @Value("${historikk.kafka.meldinger.inntektsmelding_topic}") String topic,
            ObjectMapperWrapper mapper) {
        super(topic, kafkaOperations, mapper);
    }

}
