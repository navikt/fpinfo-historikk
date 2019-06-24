package no.nav.foreldrepenger.historikk.tjenester.journalføring;

import java.net.URI;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import no.nav.foreldrepenger.historikk.util.URIUtil;

@ConfigurationProperties(prefix = "journalføring")
@Configuration
public class JournalføringConfig {

    private static final String SERVICE = "http://dokarkiv";
    private static final String BASE_PATH = "/rest/journalpostapi/v1/";
    private static final URI DEFAULT_BASE_URI = URI.create(SERVICE + BASE_PATH);
    private static final String DEFAULT_PING_PATH = "/actuator";
    private boolean enabled;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public URI pingURI() {
        return URIUtil.uri(SERVICE, DEFAULT_PING_PATH);
    }
}
