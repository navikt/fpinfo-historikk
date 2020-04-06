package no.nav.foreldrepenger.historikk.tjenester.aktør;

import static no.nav.foreldrepenger.historikk.config.Constants.NAV_CALL_ID1;
import static no.nav.foreldrepenger.historikk.config.Constants.NAV_PERSONIDENTER;

import java.net.URI;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import no.nav.foreldrepenger.historikk.domain.AktørId;
import no.nav.foreldrepenger.historikk.http.AbstractRestConnection;
import no.nav.foreldrepenger.historikk.util.MDCUtil;
import no.nav.foreldrepenger.historikk.util.TokenUtil;

@Component
public class AktørConnection extends AbstractRestConnection {

    private static final Logger LOG = LoggerFactory.getLogger(AktørConnection.class);
    private final AktørConfig cfg;
    private final TokenUtil tokenUtil;

    public AktørConnection(RestTemplate restOperations, AktørConfig cfg, TokenUtil tokenUtil) {
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
        if (cfg.isEnabled()) {
            LOG.info("Henter aktør for {} via REST", tokenUtil.autentisertBruker());
            var headers = new HttpHeaders();
            headers.add(NAV_PERSONIDENTER, tokenUtil.autentisertBruker());
            headers.add(NAV_CALL_ID1, MDCUtil.callId());
            var map = exchangeGet(cfg.aktørUri(), Map.class, headers);
            LOG.info("Fikk map {}", map);
        }
        LOG.info("Aktørtjenesten er disabled");
        return AktørId.valueOf("42");
    }
}
