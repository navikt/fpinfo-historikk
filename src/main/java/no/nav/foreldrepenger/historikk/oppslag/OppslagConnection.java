package no.nav.foreldrepenger.historikk.oppslag;

import static no.nav.foreldrepenger.historikk.util.URIUtil.uri;

import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestOperations;

import no.nav.foreldrepenger.historikk.domain.AktørId;
import no.nav.foreldrepenger.historikk.http.AbstractRestConnection;
import no.nav.foreldrepenger.historikk.http.PingEndpointAware;

@Component
public class OppslagConnection extends AbstractRestConnection implements PingEndpointAware {
    public static final Logger LOG = LoggerFactory.getLogger(OppslagConnection.class);
    private final OppslagConfig cfg;

    public OppslagConnection(RestOperations restOperations, OppslagConfig config) {
        super(restOperations);
        this.cfg = config;
    }

    @Override
    public String ping() {
        return ping(pingEndpoint());
    }

    @Override
    public URI pingEndpoint() {
        return uri(cfg.getBaseURI(), cfg.getPingPath());
    }

    public AktørId hentAktørId() {
        return getForObject(uri(cfg.getBaseURI(), cfg.getAktørPath()), AktørId.class, true);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [cfg=" + cfg + "]";
    }

    @Override
    public String name() {
        return "fpsoknad-oppslag";
    }
}
