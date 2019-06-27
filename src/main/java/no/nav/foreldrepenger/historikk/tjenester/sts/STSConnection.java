package no.nav.foreldrepenger.historikk.tjenester.sts;

import static no.nav.foreldrepenger.historikk.config.RestClientConfiguration.STS;

import java.net.URI;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestOperations;

import no.nav.foreldrepenger.historikk.http.AbstractRestConnection;
import no.nav.foreldrepenger.historikk.http.PingEndpointAware;
import no.nav.foreldrepenger.historikk.tjenester.oppslag.TokenRespons;

@Component
public class STSConnection extends AbstractRestConnection implements PingEndpointAware {
    public static final Logger LOG = LoggerFactory.getLogger(STSConnection.class);
    private final STSConfig cfg;

    public STSConnection(@Qualifier(STS) RestOperations restOperations, STSConfig config) {
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

    @Override
    public boolean isEnabled() {
        return cfg.isEnabled();
    }

    @Override
    public String name() {
        return pingEndpoint().getHost();
    }

    public String hentToken() {
        if (isEnabled()) {
            return Optional.ofNullable(getForObject(cfg.tokenURI(), TokenRespons.class, true))
                    .map(TokenRespons::getToken)
                    .orElse(null);
        }
        return null;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [cfg=" + cfg + "]";
    }
}
