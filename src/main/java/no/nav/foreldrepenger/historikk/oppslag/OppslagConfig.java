package no.nav.foreldrepenger.historikk.oppslag;

import java.net.URI;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import no.nav.foreldrepenger.historikk.util.URIUtil;

@ConfigurationProperties(prefix = "oppslag")
@Configuration
public class OppslagConfig {
    private static final String AKTØR = "oppslag/aktor";
    private static final URI DEFAULT_BASE_URI = URI.create("http://fpsoknad-oppslag/api");
    private static final String DEFAULT_PING_PATH = "actuator/info";
    private boolean enabled;

    public URI aktørURI() {
        return URIUtil.uri(DEFAULT_BASE_URI, AKTØR);
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public URI pingURI() {
        return URIUtil.uri(DEFAULT_BASE_URI, DEFAULT_PING_PATH);
    }
}
