package no.nav.foreldrepenger.historikk.tjenester.journalføring;

import java.net.URI;
import java.util.Optional;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import no.nav.foreldrepenger.historikk.util.URIUtil;

@ConfigurationProperties(prefix = "journal")
@Configuration
public class JournalføringConfig {

    private static final String SERVICE = "http://dokarkiv";
    private static final String BASE_PATH = "/rest/journalpostapi/v1/";
    private static final URI DEFAULT_BASE_URI = URI.create(SERVICE + BASE_PATH);
    private static final String DEFAULT_PING_PATH = "actuator";
    private boolean enabled;
    private String service;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getService() {
        return Optional.ofNullable(service).orElse(SERVICE);
    }

    public URI pingURI() {
        return URIUtil.uri(getService(), DEFAULT_PING_PATH);
    }
}
