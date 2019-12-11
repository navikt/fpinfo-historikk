package no.nav.foreldrepenger.historikk.tjenester.sts;

import java.net.URI;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.web.util.UriComponentsBuilder;

import no.nav.foreldrepenger.historikk.http.AbstractConfig;

@ConfigurationProperties("sts")
public class STSConfig extends AbstractConfig {

    static final String SERVICE = "http://security-token-service/rest";
    private static final String TOKEN_PATH = "/v1/sts/token";
    private static final String DEFAULT_PING_PATH = ".well-known/openid-configuration";
    private final URI service;

    public STSConfig(@DefaultValue({ SERVICE }) URI service) {
        this.service = service;
    }

    public URI getService() {
        return service;
    }

    @Override
    public URI pingURI() {
        return uri(getService(), DEFAULT_PING_PATH);
    }

    public URI tokenURI() {
        return UriComponentsBuilder.fromUri(uri(getService(), TOKEN_PATH))
                .queryParam("grant_type", "client_credentials")
                .queryParam("scope", "openid")
                .build().toUri();
    }
}
