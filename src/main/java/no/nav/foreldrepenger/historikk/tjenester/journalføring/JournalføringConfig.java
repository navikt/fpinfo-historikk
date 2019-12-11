package no.nav.foreldrepenger.historikk.tjenester.journalføring;

import java.net.URI;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.http.HttpHeaders;

import no.nav.foreldrepenger.historikk.http.AbstractConfig;

@ConfigurationProperties(prefix = "dokarkiv", ignoreInvalidFields = true, ignoreUnknownFields = true)
public class JournalføringConfig extends AbstractConfig {

    private static final String SERVICE = "http://dokarkiv";
    private static final String BASE_PATH = "/rest/journalpostapi/v1/";
    private static final String DEFAULT_PING_PATH = "actuator";
    private final URI service;

    public JournalføringConfig(@DefaultValue({ SERVICE }) URI service) {
        this.service = service;
    }

    public URI getService() {
        return service;
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
