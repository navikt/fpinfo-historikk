package no.nav.foreldrepenger.historikk.tjenester.sts;

import java.net.URI;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestOperations;

import no.nav.foreldrepenger.historikk.http.AbstractRestConnection;
import no.nav.foreldrepenger.historikk.http.PingEndpointAware;

@Component
public class STSConnection extends AbstractRestConnection implements PingEndpointAware {
    public static final Logger LOG = LoggerFactory.getLogger(STSConnection.class);
    private final STSConfig cfg;

    public STSConnection(@Qualifier("sts") RestOperations restOperations, STSConfig config) {
        super(restOperations);
        this.cfg = config;
    }

    @Override
    public String ping() {
        return ping(pingEndpoint());
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
        return "sts";
    }

    public String hentToken() {
        Map<String, String> map = getForObject(cfg.tokenURI(), Map.class, true);
        return Optional.ofNullable(map).map(m -> m.get("access_token")).orElse(null);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [cfg=" + cfg + "]";
    }
}
