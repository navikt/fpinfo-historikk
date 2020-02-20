package no.nav.foreldrepenger.historikk.tjenester.dittnav;

import static no.nav.foreldrepenger.historikk.tjenester.innsending.InnsendingMapper.tilBeskjedDTO;
import static no.nav.foreldrepenger.historikk.tjenester.minidialog.MinidialogMapper.tilDoneDTO;
import static no.nav.foreldrepenger.historikk.tjenester.minidialog.MinidialogMapper.tilOppgaveDTO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import no.nav.foreldrepenger.historikk.tjenester.innsending.InnsendingHendelse;
import no.nav.foreldrepenger.historikk.tjenester.minidialog.MinidialogHendelse;

@Service
@ConditionalOnProperty(name = "historikk.dittnav.enabled", havingValue = "false")
public class LoggingDittNavMeldingProdusent implements DittNavOperasjoner {

    private static final String URL = "https://foreldrepengesoknad.nav.no";
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
        opprettBeskjed(tilBeskjedDTO(h, URL));
    }

    @Override
    public void opprettOppgave(MinidialogHendelse h) {
        opprettOppgave(tilOppgaveDTO(h, URL));
    }

    @Override
    public void avsluttOppgave(MinidialogHendelse h) {
        avsluttOppgave(tilDoneDTO(h));
    }
}
