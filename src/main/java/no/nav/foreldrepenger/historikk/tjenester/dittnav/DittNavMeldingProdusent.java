package no.nav.foreldrepenger.historikk.tjenester.dittnav;

import static no.nav.foreldrepenger.historikk.config.TxConfiguration.KAFKA_TM;
import static no.nav.foreldrepenger.historikk.tjenester.dittnav.DittNavMapper.avslutt;
import static no.nav.foreldrepenger.historikk.tjenester.dittnav.DittNavMapper.beskjed;
import static no.nav.foreldrepenger.historikk.tjenester.dittnav.DittNavMapper.beskjedNøkkel;
import static no.nav.foreldrepenger.historikk.tjenester.dittnav.DittNavMapper.oppgave;
import static no.nav.foreldrepenger.historikk.tjenester.dittnav.DittNavMapper.oppgaveNøkkel;

import java.util.UUID;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaOperations;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.concurrent.ListenableFutureCallback;

import no.nav.brukernotifikasjon.schemas.Nokkel;
import no.nav.foreldrepenger.historikk.domain.Fødselsnummer;
import no.nav.foreldrepenger.historikk.tjenester.felles.LandingssideGenerator;
import no.nav.foreldrepenger.historikk.tjenester.innsending.InnsendingHendelse;

@Service
public class DittNavMeldingProdusent implements DittNav {

    private static final Logger LOG = LoggerFactory.getLogger(DittNavMeldingProdusent.class);

    private final KafkaOperations<Nokkel, Object> kafkaOperations;
    private final DittNavConfig config;
    private final DittNavOppgave oppgave;
    private final LandingssideGenerator landingssideGenerator;

    public DittNavMeldingProdusent(KafkaOperations<Nokkel, Object> kafkaOperations,
            DittNavConfig config, DittNavOppgave oppgave, LandingssideGenerator landingssideGenerator) {
        this.kafkaOperations = kafkaOperations;
        this.config = config;
        this.oppgave = oppgave;
        this.landingssideGenerator = landingssideGenerator;
    }

    @Transactional(KAFKA_TM)
    @Override
    public void avsluttOppgave(Fødselsnummer fnr, String grupperingsId, String eventId) {
        var key = oppgaveNøkkel(eventId);
        if (oppgave.slett(key.getEventId())) {
            LOG.info("Avslutter oppgave med eventId  {} for {} {} i Ditt Nav", key.getEventId(), fnr, grupperingsId);
            send(avslutt(fnr, grupperingsId), key, config.getDone());
        } else {
            LOG.info("Ingen oppgave å avslutte i Ditt Nav");
        }
    }

    @Transactional(KAFKA_TM)
    @Override
    public void avsluttBeskjed(Fødselsnummer fnr, String grupperingsId, String eventId) {
        var key = beskjedNøkkel(eventId);
        if (oppgave.slett(key.getEventId())) {
            LOG.info("Avslutter beskjed med eventId  {} for {} {} i Ditt Nav", key.getEventId(), fnr, grupperingsId);
            send(avslutt(fnr, grupperingsId), key, config.getDone());
        } else {
            LOG.info("Ingen beskjed å avslutte i Ditt Nav");
        }
    }

    @Override
    @Transactional(KAFKA_TM)
    public void opprettBeskjed(InnsendingHendelse h, String tekst) {
        var key = beskjedNøkkel(h.getReferanseId());
        var url = landingssideGenerator.uri(h.getHendelse());
        if (h.getSaksnummer() != null) {
            LOG.info("Oppretter beskjed for med eventId {} {} {} {} {} i Ditt Nav", key.getEventId(), h.getFnr(), h.getSaksnummer(),
                    tekst, url);
            send(beskjed(h.getFnr(), h.getSaksnummer(), tekst, url, config.getVarighet()), key, config.getBeskjed());
        } else {
            LOG.info("Kan ikke gruppere beskjed i Ditt Nav uten grupperingsId(saksnr), bruker random verdi");
            send(beskjed(h.getFnr(), UUID.randomUUID().toString(), tekst, url, config.getVarighet()), key,
                    config.getBeskjed());
        }
        oppgave.opprett(h.getFnr(), key.getEventId(), h.getSaksnummer(), h.getHendelse());
    }

    @Override
    @Transactional(KAFKA_TM)
    public void opprettOppgave(InnsendingHendelse h, String tekst) {
        if (!oppgave.erOpprettet(h.getSaksnummer(), h.getHendelse())) {
            var key = oppgaveNøkkel(h.getReferanseId());
            var url = landingssideGenerator.uri(h.getHendelse());
            LOG.info("Oppretter oppgave for med eventId {} {} {} {} {} i Ditt Nav", key.getEventId(), h.getFnr(), h.getSaksnummer(),
                    tekst, url);
            send(oppgave(h.getFnr(), h.getSaksnummer(), tekst, url), key, config.getOppgave());
            oppgave.opprett(h.getFnr(), key.getEventId(), h.getSaksnummer(), h.getHendelse());
        } else {
            LOG.info("Det finnes allerede en oppgave av type {} for sak {}", h.getHendelse(), h.getSaksnummer());
        }
    }

    private void send(Object msg, Nokkel key, String topic) {
        var melding = new ProducerRecord<>(topic, key, msg);
        LOG.info("Sender melding med id {} på {}", key.getEventId(), topic);
        kafkaOperations.send(melding).addCallback(new ListenableFutureCallback<SendResult<Nokkel, Object>>() {

            @Override
            public void onSuccess(SendResult<Nokkel, Object> result) {
                LOG.info("Sendte melding med id {} og offset {} på {}", key.getEventId(),
                        result.getRecordMetadata().offset(), topic);
            }

            @Override
            public void onFailure(Throwable e) {
                LOG.warn("Kunne ikke sende melding {} med id {} på {}", msg, key.getEventId(), topic, e);
            }
        });
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [kafkaOperations=" + kafkaOperations + ", config=" + config + ", oppgave=" + oppgave
                + ", landingssideGenerator=" + landingssideGenerator + "]";
    }

}
