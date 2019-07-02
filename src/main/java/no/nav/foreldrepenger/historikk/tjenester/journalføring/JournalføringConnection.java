package no.nav.foreldrepenger.historikk.tjenester.journalføring;

import static no.nav.foreldrepenger.historikk.config.RestClientConfiguration.DOKARKIV;
import static no.nav.foreldrepenger.historikk.tjenester.journalføring.Journalstatus.ENDELIG;

import java.net.URI;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestOperations;

import no.nav.foreldrepenger.historikk.errorhandling.UnexpectedResponseException;
import no.nav.foreldrepenger.historikk.http.AbstractRestConnection;
import no.nav.foreldrepenger.historikk.http.PingEndpointAware;

@Component
public class JournalføringConnection extends AbstractRestConnection implements PingEndpointAware {
    private final JournalføringConfig cfg;

    public JournalføringConnection(@Qualifier(DOKARKIV) RestOperations restOperations,
            JournalføringConfig config) {
        super(restOperations, config.isEnabled());
        this.cfg = config;
    }

    @Override
    public String ping() {
        return ping(pingEndpoint());
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
        throw new UnexpectedResponseException("Kunne ikke journalføre, ingen respons");
    }

    @Override
    public URI pingEndpoint() {
        return cfg.pingURI();
    }

    @Override
    public boolean isEnabled() {
        return cfg.isEnabled();
    }

    @Override
    public String name() {
        return pingEndpoint().getHost();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [cfg=" + cfg + "]";
    }

}
