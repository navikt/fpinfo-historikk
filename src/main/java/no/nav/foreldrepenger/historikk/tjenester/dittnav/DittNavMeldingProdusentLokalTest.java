package no.nav.foreldrepenger.historikk.tjenester.dittnav;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaOperations;
import org.springframework.stereotype.Service;

import no.nav.boot.conditionals.ConditionalOnLocalOrTest;
import no.nav.brukernotifikasjon.schemas.input.NokkelInput;
import no.nav.foreldrepenger.historikk.tjenester.innsending.InnsendingHendelse;

@Service
@ConditionalOnLocalOrTest
public class DittNavMeldingProdusentLokalTest implements DittNav {

    private static final Logger LOG = LoggerFactory.getLogger(DittNavMeldingProdusentLokalTest.class);

    private final DittNavConfig config;
    private final DittNavMeldingsHistorikk lager;
    private final KafkaOperations<NokkelInput, Object> kafkaOperations;

    public DittNavMeldingProdusentLokalTest(DittNavConfig config,
                                            DittNavMeldingsHistorikk lager,
                                            KafkaOperations<NokkelInput, Object> kafkaOperations) {
        this.config = config;
        this.lager = lager;
        this.kafkaOperations = kafkaOperations;
    }

    @Override
    public void avsluttOppgave(String eventId) {
        LOG.info("Avslutter oppgave {}", eventId);
    }

    @Override
    public void opprettBeskjed(InnsendingHendelse h, String tekst) {
        LOG.info("Oppretter beskjed {}", h);
    }

    @Override
    public void opprettOppgave(InnsendingHendelse h, String tekst) {
        LOG.info("Oppretter oppgave {}", h);
    }


    @Override
    public String toString() {
        return "DittNavMeldingProdusent{" +
            "config=" + config +
            ", lager=" + lager +
            ", kafkaOperations=" + kafkaOperations +
            '}';
    }
}
