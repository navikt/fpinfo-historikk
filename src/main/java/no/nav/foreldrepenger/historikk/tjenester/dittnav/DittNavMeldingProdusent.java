package no.nav.foreldrepenger.historikk.tjenester.dittnav;

import static no.nav.foreldrepenger.historikk.tjenester.dittnav.DittNavMapper.avslutt;

import java.time.Duration;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaOperations;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFutureCallback;

import no.nav.brukernotifikasjon.schemas.Nokkel;
import no.nav.foreldrepenger.historikk.domain.Fødselsnummer;
import no.nav.foreldrepenger.historikk.tjenester.felles.HendelseType;
import no.nav.foreldrepenger.historikk.tjenester.felles.UrlGenerator;

@Service
@ConditionalOnProperty(name = "historikk.dittnav.enabled", havingValue = "true")
public class DittNavMeldingProdusent implements DittNav {

    private static final String SYSTEMBRUKER = "srvfpinfo-historikk";

    private static final Logger LOG = LoggerFactory.getLogger(DittNavMeldingProdusent.class);
    private final UrlGenerator urlGenerator;

    private final KafkaOperations<Nokkel, Object> kafkaOperations;

    private final DittNavConfig config;

    private final Duration varighet;

    public DittNavMeldingProdusent(UrlGenerator urlGenerator, KafkaOperations<Nokkel, Object> kafkaOperations,
            DittNavConfig config, @Value("${dittnav.beskjed.levetid:30d}") Duration varighet) {
        this.urlGenerator = urlGenerator;
        this.kafkaOperations = kafkaOperations;
        this.config = config;
        this.varighet = varighet;
    }

    // @Transactional(KAFKA_TM)
    @Override
    public void avsluttOppgave(Fødselsnummer fnr, String grupperingsId,
            String eventId) {
        LOG.info("Avslutter oppgave for {} {} {} i Ditt Nav", fnr,
                grupperingsId, eventId);
        send(avslutt(fnr, grupperingsId), eventId,
                config.getTopics().getAvslutt());
    }

    @Override
    // @Transactional(KAFKA_TM)
    public void opprettBeskjed(Fødselsnummer fnr, String grupperingsId, String eventId, String tekst, HendelseType h) {
        if (grupperingsId != null) {
            LOG.info("Oppretter beskjed med id {} for {} {} {} {} i Ditt Nav", eventId, fnr, grupperingsId, tekst,
                    h.beskrivelse);
            // send(beskjed(fnr, grupperingsId, tekst + h.beskrivelse, urlGenerator.url(h),
            // varighet),
            // eventId, config.getTopics().getBeskjed());
        } else {
            LOG.info("Kan ikke opprette beskjed i Ditt Nav uten grupperingsId(saksnr)");
        }
    }

    @Override
    // @Transactional(KAFKA_TM)
    public void opprettOppgave(Fødselsnummer fnr, String grupperingsId, String eventId, String tekst, HendelseType h) {
        LOG.info("Oppretter oppgave for {} {} {} {} {} i Ditt Nav", fnr,
                grupperingsId, tekst, h.beskrivelse, eventId);
        // send(oppgave(fnr,
        // grupperingsId, tekst, urlGenerator.url(h)), eventId,
        // config.getTopics().getOpprett());

    }

    private void send(Object msg, String eventId, String topic) {
        ProducerRecord<Nokkel, Object> melding = new ProducerRecord<>(topic, beskjedNøkkel(eventId), msg);
        LOG.info("Sender melding med id {} på {}", eventId, topic);
        kafkaOperations.send(melding).addCallback(new ListenableFutureCallback<SendResult<Nokkel, Object>>() {

            @Override
            public void onSuccess(SendResult<Nokkel, Object> result) {
                LOG.info("Sendte melding med id {} og offset {} på {}", eventId,
                        result.getRecordMetadata().offset(), topic);
            }

            @Override
            public void onFailure(Throwable e) {
                LOG.warn("Kunne ikke sende melding med id {} på {}", eventId, topic, e);
            }
        });
    }

    private static Nokkel beskjedNøkkel(String eventId) {
        var nøkkel = Nokkel.newBuilder()
                .setEventId(eventId)
                .setSystembruker(SYSTEMBRUKER)
                .build();
        LOG.info("Bruker nøkkel med eventId {} og systembruker {}", nøkkel.getEventId(), nøkkel.getSystembruker());
        return nøkkel;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[urlGenerator=" + urlGenerator + ", kafkaOperations=" + kafkaOperations
                + ", config=" + config + "]";
    }

}
