package no.nav.foreldrepenger.historikk.tjenester.oppslag;

import no.nav.foreldrepenger.historikk.domain.AktørId;
import no.nav.foreldrepenger.historikk.http.AbstractRestConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestOperations;

import java.net.URI;

@Component
public class OppslagConnectionRestTemplate extends AbstractRestConnection {
    public static final Logger LOG = LoggerFactory.getLogger(OppslagConnectionRestTemplate.class);
    private final OppslagConfig cfg;

    public OppslagConnectionRestTemplate(RestOperations restOperations, OppslagConfig config) {
        super(restOperations, config);
        this.cfg = config;
    }

    @Override
    public URI pingEndpoint() {
        return cfg.pingEndpoint();
    }

    @Cacheable(cacheNames = "organisasjon")
    public String orgNavn(String orgnr) {
        return getForObject(cfg.orgNavnURI(orgnr));
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
