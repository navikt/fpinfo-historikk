package no.nav.foreldrepenger.historikk.tjenester.dittnav;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import no.nav.foreldrepenger.historikk.domain.Fødselsnummer;
import no.nav.foreldrepenger.historikk.tjenester.innsending.InnsendingHendelse;
import no.nav.foreldrepenger.historikk.tjenester.minidialog.MinidialogHendelse;

@Service
@ConditionalOnProperty(name = "historikk.dittnav.enabled", havingValue = "false")
public class LoggingDittNavMeldingProdusent implements DittNavOperasjoner {

    private static final Logger LOG = LoggerFactory.getLogger(LoggingDittNavMeldingProdusent.class);

    @Override
    public void opprettBeskjed(InnsendingHendelse h) {
        LOG.info("Oppretter beskjed fra {} i Ditt Nav", h);
    }

    @Override
    public void opprettOppgave(MinidialogHendelse h) {
        LOG.info("Oppretter oppgave fra {} i Ditt Nav", h);
    }

    @Override
    public void avsluttOppgave(Fødselsnummer fnr, String grupperingsId, String eventId) {
        LOG.info("Avslutter oppgave for {} {} {} i Ditt Nav", fnr, grupperingsId, eventId);
    }

    @Override
    public void opprettBeskjed(String fnr, String grupperingsId, String tekst, String url) {
        LOG.info("Oppretter beskjed for {} {} {} {} i Ditt Nav", fnr, grupperingsId, tekst, url);
    }
}
