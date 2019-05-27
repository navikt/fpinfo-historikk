package no.nav.foreldrepenger.historikk.kafka;

import static no.nav.foreldrepenger.historikk.util.EnvUtil.DEV;
import static no.nav.foreldrepenger.historikk.util.EnvUtil.PREPROD;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import no.nav.foreldrepenger.historikk.domain.Melding;
import no.nav.foreldrepenger.historikk.repository.MeldingRepository;

@Service
@Profile({ DEV, PREPROD })
public class MeldingsKonsument {
    private static final Logger LOG = LoggerFactory.getLogger(MeldingsProdusent.class);
    @Autowired
    private MeldingRepository repository;

    @KafkaListener(topics = "#{'${kafka.topic}'}", groupId = "#{'${spring.kafka.consumer.group-id}'}")
    public void consume(Melding melding) {
        LOG.info(String.format("#### -> Consumed message -> %s", melding));
        repository.save(melding);
    }
}