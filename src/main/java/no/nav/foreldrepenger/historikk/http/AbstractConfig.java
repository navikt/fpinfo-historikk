package no.nav.foreldrepenger.historikk.http;

import java.net.URI;

import org.springframework.http.HttpHeaders;
import org.springframework.web.util.UriComponentsBuilder;

public abstract class AbstractConfig {

    private final boolean enabled;

    public AbstractConfig(boolean enabled) {
        this.enabled = enabled;
    }

    protected abstract URI pingURI();

    public boolean isEnabled() {
        return enabled;
    }

    protected URI uri(String base, String path) {
        return uri(URI.create(base), path);

    }

    protected URI uri(URI base, String path) {
        return uri(base, path, null);
    }

    protected URI uri(URI base, String path, HttpHeaders queryParams) {
        return builder(base, path, queryParams)
                .build()
                .toUri();
    }

    protected HttpHeaders queryParams(String key, String value) {
        HttpHeaders queryParams = new HttpHeaders();
        queryParams.add(key, value);
        return queryParams;
    }

    private static UriComponentsBuilder builder(URI base, String path, HttpHeaders queryParams) {
        return UriComponentsBuilder
                .fromUri(base)
                .pathSegment(path)
                .queryParams(queryParams);
    }

}
