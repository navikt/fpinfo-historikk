package no.nav.foreldrepenger.historikk.tjenester.innsending;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaOperations;
import org.springframework.stereotype.Service;

import no.nav.foreldrepenger.historikk.tjenester.felles.AbstractHendelseProdusent;
import no.nav.foreldrepenger.historikk.util.ConditionalOnDevOrLocal;
import no.nav.foreldrepenger.historikk.util.ObjectMapperWrapper;

@Service
@ConditionalOnDevOrLocal
public class InnsendingHendelseProdusent extends AbstractHendelseProdusent<InnsendingHendelse> {

    public InnsendingHendelseProdusent(KafkaOperations<String, String> kafkaOperations,
            @Value("${historikk.kafka.meldinger.søknad_topic}") String søknadTopic,
            ObjectMapperWrapper mapper) {
        super(søknadTopic, kafkaOperations, mapper);
    }
}
