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
    private final String opprettOppgaveTopic;
    private final String doneOppgaveTopic;

    public DittNavMeldingProdusent(KafkaOperations<Nokkel, Oppgave> kafkaOperations,
            @Value("${historikk.kafka.meldinger.oppgave.opprett}") String opprettOppgaveTopic,
            @Value("${historikk.kafka.meldinger.oppgave.done}") String doneOppgaveTopic) {
        this.kafkaOperations = kafkaOperations;
        this.opprettOppgaveTopic = opprettOppgaveTopic;
        this.doneOppgaveTopic = doneOppgaveTopic;

    }

    @Transactional(KAFKA_TM)
    public void opprettOppgave(OppgaveDTO dto) {
        ProducerRecord<Nokkel, Oppgave> melding = new ProducerRecord<>(opprettOppgaveTopic,
                new Nokkel(SYSTEMBRUKER, dto.getEventId()), createOppgave(dto));
        kafkaOperations.send(melding).addCallback(new ListenableFutureCallback<SendResult<Nokkel, Oppgave>>() {

            @Override
            public void onSuccess(SendResult<Nokkel, Oppgave> result) {
                LOG.info("Sendte melding {} med offset {} på {}", melding.value(),
                        result.getRecordMetadata().offset(), opprettOppgaveTopic);
            }

            @Override
            public void onFailure(Throwable e) {
                LOG.warn("Kunne ikke sende melding {} på {}", melding.value(), opprettOppgaveTopic, e);
            }
        });

    }

    @Transactional(KAFKA_TM)
    public void avsluttOppgave(OppgaveDTO dto) {
        ProducerRecord<Nokkel, Oppgave> melding = new ProducerRecord<>(doneOppgaveTopic,
                new Nokkel(SYSTEMBRUKER, dto.getEventId()), createOppgave(dto));
        kafkaOperations.send(melding).addCallback(new ListenableFutureCallback<SendResult<Nokkel, Oppgave>>() {

            @Override
            public void onSuccess(SendResult<Nokkel, Oppgave> result) {
                LOG.info("Sendte melding {} med offset {} på {}", melding.value(),
                        result.getRecordMetadata().offset(), doneOppgaveTopic);
            }

            @Override
            public void onFailure(Throwable e) {
                LOG.warn("Kunne ikke sende melding {} på {}", melding.value(), doneOppgaveTopic, e);
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
