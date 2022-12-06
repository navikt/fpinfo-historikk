package no.nav.foreldrepenger.historikk.tjenester.tilbakekreving;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaOperations;
import org.springframework.stereotype.Service;

import no.nav.boot.conditionals.ConditionalOnNotProd;
import no.nav.foreldrepenger.historikk.tjenester.felles.AbstractHendelseProdusent;
import no.nav.foreldrepenger.historikk.util.ObjectMapperWrapper;

import static no.nav.foreldrepenger.historikk.config.KafkaOnpremListenerConfiguration.ONPREM;

@Service
@ConditionalOnNotProd
public class TilbakekrevingHendelseProdusent extends AbstractHendelseProdusent<TilbakekrevingHendelse> {

    public TilbakekrevingHendelseProdusent(@Qualifier(ONPREM) KafkaOperations<Object, Object> kafkaOperations,
            @Value("${historikk.tilbakekreving.topic}") String topic, ObjectMapperWrapper mapper) {
        super(topic, kafkaOperations, mapper);
    }
}
