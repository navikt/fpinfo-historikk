package no.nav.foreldrepenger.historikk.tjenester.dittnav;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import no.nav.foreldrepenger.historikk.domain.Fødselsnummer;

@Service
@ConditionalOnProperty(name = "historikk.dittnav.enabled", havingValue = "false")
public class DittNavLoggingMeldingProdusent implements DittNavOperasjoner {

    private static final Logger LOG = LoggerFactory.getLogger(DittNavLoggingMeldingProdusent.class);

    @Override
    public void avsluttOppgave(Fødselsnummer fnr, String grupperingsId, String eventId) {
        LOG.info("Avslutter oppgave for {} {} {} i Ditt Nav", fnr, grupperingsId, eventId);
    }

    @Override
    public void opprettBeskjed(Fødselsnummer fnr, String grupperingsId, String tekst, String url, String eventId) {
        LOG.info("Oppretter beskjed for {} {} {} {} {} i Ditt Nav", fnr, grupperingsId, tekst, url, eventId);
    }

    @Override
    public void opprettOppgave(Fødselsnummer fnr, String grupperingsId, String tekst, String url, String eventId) {
        LOG.info("Oppretter oppgave for {} {} {} {} {} i Ditt Nav", fnr, grupperingsId, tekst, url, eventId);

    }
}
