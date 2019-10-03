package no.nav.foreldrepenger.historikk.tjenester.oppslag;

import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestOperations;

import no.nav.foreldrepenger.historikk.domain.AktørId;
import no.nav.foreldrepenger.historikk.http.AbstractRestConnection;

@Component
public class OppslagConnection extends AbstractRestConnection {
    public static final Logger LOG = LoggerFactory.getLogger(OppslagConnection.class);
    private final OppslagConfig cfg;

    public OppslagConnection(RestOperations restOperations, OppslagConfig config) {
        super(restOperations, config);
        this.cfg = config;
    }

    @Override
    public URI pingEndpoint() {
        return cfg.pingURI();
    }

    public AktørId hentAktørId() {
        return getForObject(cfg.aktørURI(), AktørId.class, true);
    }

    public String hentNavn(String fnr) {
        return getForObject(cfg.personNavnURI(fnr), String.class);
    }

    String orgNavn(String orgnr) {
        return getForObject(cfg.orgNavnURI(orgnr), String.class);
    }

    @Override
    public boolean isEnabled() {
        return cfg.isEnabled();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [cfg=" + cfg + "]";
    }

}
