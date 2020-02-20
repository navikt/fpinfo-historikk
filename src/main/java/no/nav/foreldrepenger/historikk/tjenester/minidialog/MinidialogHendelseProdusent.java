package no.nav.foreldrepenger.historikk.tjenester.minidialog;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaOperations;
import org.springframework.stereotype.Service;

import no.nav.foreldrepenger.historikk.tjenester.felles.AbstractHendelseProdusent;
import no.nav.foreldrepenger.historikk.util.ObjectMapperWrapper;

@Service
//@ConditionalOnDevOrLocal
public class MinidialogHendelseProdusent extends AbstractHendelseProdusent<MinidialogHendelse> {

    public MinidialogHendelseProdusent(KafkaOperations<String, String> kafkaOperations,
            @Value("${historikk.kafka.topics.minidialog}") String topic, ObjectMapperWrapper mapper) {
        super(topic, kafkaOperations, mapper);
    }
}
