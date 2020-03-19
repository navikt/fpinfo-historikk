package no.nav.foreldrepenger.historikk.tjenester.tilbakekreving;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaOperations;
import org.springframework.stereotype.Service;

import no.nav.foreldrepenger.boot.conditionals.ConditionalOnNotProd;
import no.nav.foreldrepenger.historikk.tjenester.felles.AbstractHendelseProdusent;
import no.nav.foreldrepenger.historikk.util.ObjectMapperWrapper;

@Service
@ConditionalOnNotProd
public class TilbakekrevingHendelseProdusent extends AbstractHendelseProdusent<TilbakekrevingHendelse> {

    public TilbakekrevingHendelseProdusent(KafkaOperations<String, String> kafkaOperations,
            @Value("${historikk.kafka.topics.tilbakekreving}") String topic, ObjectMapperWrapper mapper) {
        super(topic, kafkaOperations, mapper);
    }
}
