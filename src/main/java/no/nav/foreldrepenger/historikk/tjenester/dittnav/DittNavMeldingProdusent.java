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

import no.nav.brukernotifikasjon.schemas.Done;
import no.nav.brukernotifikasjon.schemas.Nokkel;
import no.nav.brukernotifikasjon.schemas.Oppgave;

@Service
public class DittNavMeldingProdusent {

    private static final String SYSTEMBRUKER = "srvfpinfo-historikk";

    private static final Logger LOG = LoggerFactory.getLogger(DittNavMeldingProdusent.class);

    private final KafkaOperations<Nokkel, Object> kafkaOperations;
    private final String opprettOppgaveTopic;
    private final String doneOppgaveTopic;

    public DittNavMeldingProdusent(KafkaOperations<Nokkel, Object> kafkaOperations,
            @Value("${historikk.kafka.meldinger.oppgave.opprett}") String opprettOppgaveTopic,
            @Value("${historikk.kafka.meldinger.oppgave.done}") String doneOppgaveTopic) {
        this.kafkaOperations = kafkaOperations;
        this.opprettOppgaveTopic = opprettOppgaveTopic;
        this.doneOppgaveTopic = doneOppgaveTopic;

    }

    @Transactional(KAFKA_TM)
    public void opprettOppgave(OppgaveDTO dto) {
        ProducerRecord<Nokkel, Object> melding = new ProducerRecord<>(opprettOppgaveTopic,
                new Nokkel(SYSTEMBRUKER, dto.getEventId()), oppgave(dto));
        kafkaOperations.send(melding).addCallback(new ListenableFutureCallback<SendResult<Nokkel, Object>>() {

            @Override
            public void onSuccess(SendResult<Nokkel, Object> result) {
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
    public void avsluttOppgave(DoneDTO dto) {
        ProducerRecord<Nokkel, Object> melding = new ProducerRecord<>(doneOppgaveTopic,
                new Nokkel(SYSTEMBRUKER, dto.getEventId()), done(dto));
        kafkaOperations.send(melding).addCallback(new ListenableFutureCallback<SendResult<Nokkel, Object>>() {

            @Override
            public void onSuccess(SendResult<Nokkel, Object> result) {
                LOG.info("Sendte melding {} med offset {} på {}", melding.value(),
                        result.getRecordMetadata().offset(), doneOppgaveTopic);
            }

            @Override
            public void onFailure(Throwable e) {
                LOG.warn("Kunne ikke sende melding {} på {}", melding.value(), doneOppgaveTopic, e);
            }
        });

    }

    private static Oppgave oppgave(OppgaveDTO dto) {
        return Oppgave.newBuilder().setFodselsnummer(dto.getFnr())
                .setGrupperingsId(dto.getGrupperingsId())
                .setSikkerhetsnivaa(dto.getSikkerhetsNivå())
                .setLink(dto.getLink())
                .setTekst(dto.getTekst())
                .setTidspunkt(Instant.now().toEpochMilli()).build();
    }

    private static Done done(DoneDTO dto) {
        return Done.newBuilder().setFodselsnummer(dto.getFnr())
                .setGrupperingsId(dto.getGrupperingsId())
                .setTidspunkt(Instant.now().toEpochMilli()).build();
    }
}
