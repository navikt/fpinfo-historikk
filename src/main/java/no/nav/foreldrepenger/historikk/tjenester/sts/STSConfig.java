package no.nav.foreldrepenger.historikk.tjenester.sts;

import static no.nav.foreldrepenger.historikk.util.URIUtil.uri;

import java.net.URI;
import java.util.Optional;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.util.UriComponentsBuilder;

@ConfigurationProperties(prefix = "sts")
@Configuration
public class STSConfig {

    private static final URI SERVICE = URI.create("http://security-token-service/rest");
    private static final String TOKEN_PATH = "/v1/sts/token";
    private static final String DEFAULT_PING_PATH = ".well-known/openid-configuration";
    private boolean enabled = true;
    private URI service;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setService(URI service) {
        this.service = service;
    }

    public URI getService() {
        return Optional.ofNullable(service).orElse(SERVICE);
    }

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
