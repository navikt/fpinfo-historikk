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
public class DittNavMeldingProdusent {

    private static final String SYSTEMBRUKER = "srvfpinfo-historikk";

    private static final Logger LOG = LoggerFactory.getLogger(DittNavMeldingProdusent.class);

    private final KafkaOperations<Nokkel, Oppgave> kafkaOperations;
    private final String topic;

    public DittNavMeldingProdusent(KafkaOperations<Nokkel, Oppgave> kafkaOperations,
            @Value("${historikk.kafka.meldinger.oppgave}") String topic) {
        this.kafkaOperations = kafkaOperations;
        this.topic = topic;
    }

    @Transactional(KAFKA_TM)
    public void opprettOppgave(OppgaveDTO dto) {
        ProducerRecord<Nokkel, Oppgave> melding = new ProducerRecord<>(topic, new Nokkel(SYSTEMBRUKER, dto.getEventId()), createOppgave(dto));
        kafkaOperations.send(melding).addCallback(new ListenableFutureCallback<SendResult<Nokkel, Oppgave>>() {

            @Override
            public void onSuccess(SendResult<Nokkel, Oppgave> result) {
                LOG.info("Sendte melding {} med offset {} på {}", melding.value(),
                        result.getRecordMetadata().offset(), topic);
            }

            @Override
            public void onFailure(Throwable e) {
                LOG.warn("Kunne ikke sende melding {} på {}", melding.value(), topic, e);
            }
        });

    }

    private static Oppgave createOppgave(OppgaveDTO dto) {
        return Oppgave.newBuilder().setFodselsnummer(dto.getFnr())
                .setGrupperingsId(dto.getGrupperingsId())
                .setSikkerhetsnivaa(dto.getSikkerhetsNivå())
                .setLink(dto.getLink())
                .setTekst(dto.getTekst())
                .setTidspunkt(Instant.now().toEpochMilli()).build();
    }
}
