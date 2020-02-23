package no.nav.foreldrepenger.historikk.tjenester.dittnav;

import static no.nav.foreldrepenger.historikk.config.TxConfiguration.KAFKA_TM;
import static no.nav.foreldrepenger.historikk.tjenester.dittnav.DittNavMapper.beskjed;
import static no.nav.foreldrepenger.historikk.tjenester.dittnav.DittNavMapper.avslutt;
import static no.nav.foreldrepenger.historikk.tjenester.dittnav.DittNavMapper.oppgave;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaOperations;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.concurrent.ListenableFutureCallback;

import no.nav.brukernotifikasjon.schemas.Nokkel;
import no.nav.foreldrepenger.historikk.domain.Fødselsnummer;

@Service
@ConditionalOnProperty(name = "historikk.dittnav.enabled", havingValue = "true")
public class DittNavMeldingProdusent implements DittNavOperasjoner {

    private static final String SYSTEMBRUKER = "srvfpinfo-historikk";

    private static final Logger LOG = LoggerFactory.getLogger(DittNavMeldingProdusent.class);

    private final KafkaOperations<Nokkel, Object> kafkaOperations;

    private final DittNavTopics topics;

    public DittNavMeldingProdusent(KafkaOperations<Nokkel, Object> kafkaOperations, DittNavTopics topics) {
        this.kafkaOperations = kafkaOperations;
        this.topics = topics;
    }

    @Transactional(KAFKA_TM)
    @Override
    public void avsluttOppgave(Fødselsnummer fnr, String grupperingsId, String eventId) {
        send(avslutt(fnr, grupperingsId), eventId, topics.getAvsluttOppgaveTopic());
    }

    @Override
    @Transactional(KAFKA_TM)
    public void opprettBeskjed(Fødselsnummer fnr, String grupperingsId, String tekst, String url, String eventId) {
        send(beskjed(fnr, grupperingsId, tekst, url), eventId, topics.getBeskjedTopic());
    }

    @Override
    @Transactional(KAFKA_TM)
    public void opprettOppgave(Fødselsnummer fnr, String grupperingsId, String tekst, String url, String eventId) {
        send(oppgave(fnr, grupperingsId, tekst, url), eventId, topics.getOpprettOppgaveTopic());
    }

    private void send(Object msg, String eventId, String topic) {
        ProducerRecord<Nokkel, Object> melding = new ProducerRecord<>(topic, nøkkel(eventId), msg);
        kafkaOperations.send(melding).addCallback(new ListenableFutureCallback<SendResult<Nokkel, Object>>() {

            @Override
            public void onSuccess(SendResult<Nokkel, Object> result) {
                LOG.info("Sendte melding {} med offset {} på {}", melding.value(),
                        result.getRecordMetadata().offset(), topic);
            }

            @Override
            public void onFailure(Throwable e) {
                LOG.warn("Kunne ikke sende melding {} på {}", melding.value(), topic, e);
            }
        });
    }

    private static Nokkel nøkkel(String eventId) {
        var nøkkel = Nokkel.newBuilder()
                .setEventId(eventId)
                .setSystembruker(SYSTEMBRUKER)
                .build();
        LOG.info("Bruker nøkkel med eventId {} og systembruker {}", nøkkel.getEventId(), nøkkel.getSystembruker());
        return nøkkel;
    }

}
