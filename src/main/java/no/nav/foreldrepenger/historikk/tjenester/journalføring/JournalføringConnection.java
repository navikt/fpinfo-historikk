package no.nav.foreldrepenger.historikk.tjenester.journalføring;

import static no.nav.foreldrepenger.historikk.config.RestClientConfiguration.DOKARKIV;

import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestOperations;

import no.nav.foreldrepenger.historikk.http.AbstractRestConnection;
import no.nav.foreldrepenger.historikk.http.PingEndpointAware;

@Component
public class JournalføringConnection extends AbstractRestConnection implements PingEndpointAware {
    public static final Logger LOG = LoggerFactory.getLogger(JournalføringConnection.class);
    private final JournalføringConfig cfg;

    public JournalføringConnection(@Qualifier(DOKARKIV) RestOperations restOperations,
            JournalføringConfig config) {
        super(restOperations);
        this.cfg = config;
    }

    @Override
    public String ping() {
        return ping(pingEndpoint());
    }

    public JournalføringRespons opprettJournalpost(Journalpost journalpost, boolean sluttfør) {
        return postForEntity(cfg.journalpostURI(sluttfør), new HttpEntity<>(journalpost), JournalføringRespons.class)
                .getBody();
    }

    @Override
    public URI pingEndpoint() {
        return cfg.pingURI();
    }

    public boolean isEnabled() {
        return cfg.isEnabled();
    }

    @Override
    public String name() {
        return "dokarkiv";
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [cfg=" + cfg + "]";
    }

}
