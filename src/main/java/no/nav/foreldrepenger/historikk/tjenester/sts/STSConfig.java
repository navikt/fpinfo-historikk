package no.nav.foreldrepenger.historikk.tjenester.sts;

import java.net.URI;
import java.util.Optional;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.util.UriComponentsBuilder;

import no.nav.foreldrepenger.historikk.http.AbstractConfig;

@ConfigurationProperties(prefix = "sts")
@Configuration
public class STSConfig extends AbstractConfig {

    private static final URI SERVICE = URI.create("http://security-token-service/rest");
    private static final String TOKEN_PATH = "/v1/sts/token";
    private static final String DEFAULT_PING_PATH = ".well-known/openid-configuration";
    private URI service;

    public void setService(URI service) {
        this.service = service;
    }

    public URI getService() {
        return Optional.ofNullable(service).orElse(SERVICE);
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
