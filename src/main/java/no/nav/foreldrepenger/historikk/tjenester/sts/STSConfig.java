package no.nav.foreldrepenger.historikk.tjenester.sts;

import java.net.URI;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.web.util.UriComponentsBuilder;

import no.nav.foreldrepenger.historikk.http.AbstractConfig;

@ConfigurationProperties("historikk.sts")
public class STSConfig extends AbstractConfig {

    static final String URI = "http://security-token-service/rest";
    private static final String TOKEN_PATH = "/v1/sts/token";
    private static final String DEFAULT_PING_PATH = ".well-known/openid-configuration";
    private final URI uri;

    @ConstructorBinding
    public STSConfig(@DefaultValue({ URI }) URI uri, boolean enabled) {
        super(enabled);
        this.uri = uri;
    }

    public URI getUri() {
        return uri;
    }

    @Override
    public URI pingURI() {
        return uri(getUri(), DEFAULT_PING_PATH);
    }

    public URI tokenURI() {
        return UriComponentsBuilder.fromUri(uri(getUri(), TOKEN_PATH))
                .queryParam("grant_type", "client_credentials")
                .queryParam("scope", "openid")
                .build().toUri();
    }

}
