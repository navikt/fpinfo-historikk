package no.nav.foreldrepenger.historikk.tjenester.dittnav;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import no.nav.foreldrepenger.historikk.domain.Fødselsnummer;
import no.nav.foreldrepenger.historikk.tjenester.felles.UrlGenerator;

@Service
@ConditionalOnProperty(name = "historikk.dittnav.enabled", havingValue = "false")
public class DittNavLoggingMeldingProdusent implements DittNav {
    private final UrlGenerator urlGenerator;

    public DittNavLoggingMeldingProdusent(UrlGenerator urlGenerator) {
        this.urlGenerator = urlGenerator;
    }

    private static final Logger LOG = LoggerFactory.getLogger(DittNavLoggingMeldingProdusent.class);

    @Override
    public void avslutt(Fødselsnummer fnr, String grupperingsId, String eventId) {
        LOG.info("Avslutter for {} {} {} i Ditt Nav", fnr, grupperingsId, eventId);
    }

    @Override
    public void opprettBeskjed(Fødselsnummer fnr, String grupperingsId, String eventId, String tekst, String url) {
        LOG.info("Oppretter beskjed for {} {} {} {} {} i Ditt Nav", fnr, grupperingsId, tekst, eventId, url);
    }

    @Override
    public void opprettOppgave(Fødselsnummer fnr, String grupperingsId, String eventId, String tekst, String url) {
        LOG.info("Oppretter oppgave for {} {} {} {} i Ditt Nav", fnr, grupperingsId, tekst, eventId);
    }
}
