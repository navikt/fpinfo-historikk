package no.nav.foreldrepenger.historikk.tjenester.dittnav;

import static no.nav.foreldrepenger.historikk.config.TxConfiguration.KAFKA_TM;

import java.time.Instant;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaOperations;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.concurrent.ListenableFutureCallback;

import no.nav.brukernotifikasjon.schemas.Nokkel;
import no.nav.brukernotifikasjon.schemas.Oppgave;

@Service
public class DittNavOppgaveProdusent {

    private static final Logger LOG = LoggerFactory.getLogger(DittNavOppgaveProdusent.class);

    private final KafkaOperations<Nokkel, Oppgave> kafkaOperations;
    private final String topic;

    public DittNavOppgaveProdusent(KafkaOperations<Nokkel, Oppgave> kafkaOperations,
            @Value("${historikk.kafka.meldinger.oppgave}") String topic) {
        this.kafkaOperations = kafkaOperations;
        this.topic = topic;
    }

    @Transactional(KAFKA_TM)
    public void produser() {
        Oppgave oppgave = createOppgave();
        Long unikEventIdForDenneSystembrukeren = Instant.now().toEpochMilli();
        Nokkel nokkel = new Nokkel("enSystemBruker", unikEventIdForDenneSystembrukeren.toString());
        final ProducerRecord<Nokkel, Oppgave> record = new ProducerRecord<>(topic, nokkel, oppgave);
        kafkaOperations.send(record).addCallback(new ListenableFutureCallback<SendResult<Nokkel, Oppgave>>() {

            @Override
            public void onSuccess(SendResult<Nokkel, Oppgave> result) {
                LOG.info("Sendte melding {} med offset {} på {}", record.value(),
                        result.getRecordMetadata().offset(), topic);
            }

            @Override
            public void onFailure(Throwable e) {
                LOG.warn("Kunne ikke sende melding {} på {}", record.value(), topic, e);
            }
        });

    }

    private static Oppgave createOppgave() {
        Instant now = Instant.now();
        long tidspunkt = now.toEpochMilli();
        String fnr = "12345";
        String grupperingsId = "gruppeId1";
        String tekst = "Denne er en oppgave produsert av et eksempel. (" + now.getEpochSecond() + ")";
        String link = "https://www.vg.no";
        int sikkerhetsnivaa = 4;
        Oppgave oppgave = new Oppgave(tidspunkt, fnr, grupperingsId, tekst, link, sikkerhetsnivaa);
        return oppgave;
    }
}
