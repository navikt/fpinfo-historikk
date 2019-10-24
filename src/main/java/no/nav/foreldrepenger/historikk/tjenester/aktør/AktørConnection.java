package no.nav.foreldrepenger.historikk.tjenester.aktør;

import java.net.URI;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestOperations;

import no.nav.foreldrepenger.historikk.domain.AktørId;
import no.nav.foreldrepenger.historikk.http.AbstractRestConnection;
import no.nav.foreldrepenger.historikk.util.TokenUtil;

@Component
public class AktørConnection extends AbstractRestConnection {

    private static final Logger LOG = LoggerFactory.getLogger(AktørConnection.class);
    private final AktørConfig cfg;
    private final TokenUtil tokenUtil;

    public AktørConnection(RestOperations restOperations, AktørConfig cfg, TokenUtil tokenUtil) {
        super(restOperations, cfg);
        this.cfg = cfg;
        this.tokenUtil = tokenUtil;

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

    public AktørId aktørId() {
        var headers = new HttpHeaders();
        headers.add("Nav-Personidenter", tokenUtil.autentisertBruker());
        var map = exchangeGet(cfg.aktørUri(), Map.class, headers);
        LOG.info("Fikk map {}", map);
        return AktørId.valueOf("42");
    }
}
