package no.nav.foreldrepenger.historikk.tjenester.journalføring;

import static no.nav.foreldrepenger.historikk.config.RestClientConfiguration.DOKARKIV;
import static no.nav.foreldrepenger.historikk.tjenester.journalføring.Journalstatus.ENDELIG;

import java.net.URI;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestOperations;

import no.nav.foreldrepenger.historikk.errorhandling.UnexpectedResponseException;
import no.nav.foreldrepenger.historikk.http.AbstractRestConnection;

@Component
public class JournalføringConnection extends AbstractRestConnection {
    private final JournalføringConfig cfg;

    public JournalføringConnection(@Qualifier(DOKARKIV) RestOperations restOperations,
            JournalføringConfig config) {
        super(restOperations, config);
        this.cfg = config;
    }

    public String opprettJournalpost(Journalpost journalpost, boolean sluttfør) {
        JournalføringRespons respons = postForEntity(cfg.journalpostURI(sluttfør), journalpost,
                JournalføringRespons.class);
        if (respons != null) {
            if (!ENDELIG.equals(respons.getJournalstatus())) {
                throw new UnexpectedResponseException("Kunne ikke journalføre (" + respons.getMelding() + ")");
            }
            return respons.getJournalpostId();
        }
        throw new UnexpectedResponseException("Kunne ikke journalføre ingen respons");
    }

    @Override
    public URI pingEndpoint() {
        return cfg.pingURI();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [cfg=" + cfg + "]";
    }

}
