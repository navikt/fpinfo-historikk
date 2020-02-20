package no.nav.foreldrepenger.historikk.tjenester.innsending;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaOperations;
import org.springframework.stereotype.Service;

import no.nav.foreldrepenger.historikk.tjenester.felles.AbstractHendelseProdusent;
import no.nav.foreldrepenger.historikk.util.ObjectMapperWrapper;
import no.nav.foreldrepenger.historikk.util.annoteringer.ConditionalOnDevOrLocal;

@Service
@ConditionalOnDevOrLocal
public class InnsendingHendelseProdusent extends AbstractHendelseProdusent<InnsendingHendelse> {

    public InnsendingHendelseProdusent(KafkaOperations<String, String> kafkaOperations,
            @Value("${historikk.kafka.topics.søknad}") String søknadTopic,
            ObjectMapperWrapper mapper) {
        super(søknadTopic, kafkaOperations, mapper);
    }
}
