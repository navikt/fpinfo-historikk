package no.nav.foreldrepenger.historikk.tjenester.aktør;

import java.net.URI;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestOperations;

import no.nav.foreldrepenger.historikk.http.AbstractRestConnection;

@Component
public class AktørConnection extends AbstractRestConnection {
    private final AktørConfig cfg;

    public AktørConnection(RestOperations restOperations, AktørConfig cfg) {
        super(restOperations, cfg);
        this.cfg = cfg;

    }

    @Override
    public String ping() {
        optionsForAllow(pingEndpoint());
        return "OK";
    }

    @Override
    public URI pingEndpoint() {
        return cfg.pingURI();
    }
}
