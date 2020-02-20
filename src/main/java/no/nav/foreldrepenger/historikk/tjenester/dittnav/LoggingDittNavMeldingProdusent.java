package no.nav.foreldrepenger.historikk.tjenester.dittnav;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import no.nav.foreldrepenger.historikk.tjenester.innsending.InnsendingHendelse;
import no.nav.foreldrepenger.historikk.tjenester.innsending.InnsendingMapper;

@Service
@ConditionalOnProperty(name = "historikk.dittnav.enabled", havingValue = "false")
public class LoggingDittNavMeldingProdusent implements DittNavOperasjoner {

    private static final Logger LOG = LoggerFactory.getLogger(LoggingDittNavMeldingProdusent.class);

    @Override
    public void opprettOppgave(OppgaveDTO dto) {
        LOG.info("Oppretter oppgave fra {} i Ditt Nav", dto);
    }

    @Override
    public void avsluttOppgave(DoneDTO dto) {
        LOG.info("Avslutter oppgave fra {} i Ditt Nav", dto);
    }

    @Override
    public void opprettBeskjed(BeskjedDTO dto) {
        LOG.info("Oppretter beskjed fra {} i Ditt Nav", dto);
    }

    @Override
    public void opprettBeskjed(InnsendingHendelse h) {
        opprettBeskjed(InnsendingMapper.tilDTO(h, "https://foreldrepengesoknad.nav.no"));
    }
}
