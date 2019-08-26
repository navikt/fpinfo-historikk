package no.nav.foreldrepenger.historikk.tjenester.journalføring;

import java.net.URI;
import java.util.Optional;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

import no.nav.foreldrepenger.historikk.http.AbstractConfig;

@ConfigurationProperties(prefix = "dokarkiv", ignoreInvalidFields = true, ignoreUnknownFields = true)
@Configuration
public class JournalføringConfig extends AbstractConfig {

    private static final URI SERVICE = URI.create("http://dokarkiv");
    private static final String BASE_PATH = "/rest/journalpostapi/v1/";
    private static final String DEFAULT_PING_PATH = "actuator";
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

    public URI journalpostURI(boolean sluttfør) {
        return uri(uri(getService(), BASE_PATH), "journalpost", headers(sluttfør));
    }

    private HttpHeaders headers(boolean sluttfør) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("forsoekFerdigstill", String.valueOf(sluttfør));
        return headers;
    }
}
