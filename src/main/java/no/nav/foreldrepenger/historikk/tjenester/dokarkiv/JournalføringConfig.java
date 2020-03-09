package no.nav.foreldrepenger.historikk.tjenester.dokarkiv;

import java.net.URI;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.http.HttpHeaders;

import no.nav.foreldrepenger.historikk.http.AbstractConfig;

@ConfigurationProperties(prefix = "historikk.dokarkiv", ignoreInvalidFields = true, ignoreUnknownFields = true)
public class JournalføringConfig extends AbstractConfig {

    private static final String URI = "http://dokarkiv";
    private static final String BASE_PATH = "/rest/journalpostapi/v1/";
    private static final String DEFAULT_PING_PATH = "actuator";
    private final URI uri;

    @ConstructorBinding
    public JournalføringConfig(@DefaultValue({ URI }) URI uri, boolean enabled) {
        super(enabled);
        this.uri = uri;
    }

    public URI getUri() {
        return uri;
    }

    @Override
    public URI pingURI() {
        return uri(getUri(), DEFAULT_PING_PATH);
    }

    public URI journalpostURI(boolean sluttfør) {
        return uri(uri(getUri(), BASE_PATH), "journalpost", headers(sluttfør));
    }

    private HttpHeaders headers(boolean sluttfør) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("forsoekFerdigstill", String.valueOf(sluttfør));
        return headers;
    }
}
