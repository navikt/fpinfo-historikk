package no.nav.foreldrepenger.historikk.oppslag;

import java.net.URI;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "oppslag")
@Configuration
public class OppslagConfig {
    private static final String AKTØR = "oppslag/aktor";
    private static final URI DEFAULT_BASE_URI = URI.create("http://fpsoknad-oppslag/api");
    private static final String DEFAULT_PING_PATH = "actuator/info";
    private boolean enabled;

    public String getAktørPath() {
        return AKTØR;
    }

    public URI getBaseURI() {
        return DEFAULT_BASE_URI;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getPingPath() {
        return DEFAULT_PING_PATH;
    }

}
