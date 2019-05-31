package no.nav.foreldrepenger.historikk.oppslag;

import java.net.URI;
import java.util.Optional;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "oppslag")
@Configuration
public class OppslagConfig {
    private static final String AKTØR = "oppslag/aktor";
    private static final URI DEFAULT_BASE_URI = URI.create("http://fpsoknad-oppslag/api");
    private static final String DEFAULT_PING_PATH = "actuator/info";
    private String pingPath;
    private String aktørPath;
    private boolean enabled;
    private URI baseURI;

    public String getAktørPath() {
        return Optional.ofNullable(aktørPath).orElse(AKTØR);
    }

    public void setAktørPath(String aktørPath) {
        this.aktørPath = aktørPath;
    }

    public URI getBaseURI() {
        return Optional.ofNullable(baseURI).orElse(DEFAULT_BASE_URI);
    }

    public void setBaseURI(URI baseURI) {
        this.baseURI = baseURI;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getPingPath() {
        return Optional.ofNullable(pingPath).orElse(DEFAULT_PING_PATH);
    }

    public void setPingPath(String pingPath) {
        this.pingPath = pingPath;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [pingPath=" + pingPath + ", enabled=" + enabled + ", url=" + baseURI
                + "]";
    }
}
