package no.nav.foreldrepenger.historikk.tjenester.journalføring;

import static no.nav.foreldrepenger.historikk.util.URIUtil.uri;

import java.net.URI;
import java.util.Optional;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

@ConfigurationProperties(prefix = "dokarkiv")
@Configuration
public class JournalføringConfig {

    private static final URI SERVICE = URI.create("http://dokarkiv");
    private static final String BASE_PATH = "/rest/journalpostapi/v1/";
    private static final URI DEFAULT_BASE_URI = uri(SERVICE, BASE_PATH);
    private static final String DEFAULT_PING_PATH = "actuator";
    private boolean enabled;
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

    public URI journalpostURI(boolean sluttfør) {
        return uri(DEFAULT_BASE_URI, "journalpost", headers(sluttfør));
    }

    private HttpHeaders headers(boolean sluttfør) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("forsoekFerdigstill", String.valueOf(sluttfør));
        return headers;
    }
}
