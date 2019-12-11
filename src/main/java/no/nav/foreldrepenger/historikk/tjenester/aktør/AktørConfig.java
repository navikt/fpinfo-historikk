package no.nav.foreldrepenger.historikk.tjenester.aktør;

import java.net.URI;

import org.springframework.boot.context.properties.ConfigurationProperties;

import no.nav.foreldrepenger.historikk.http.AbstractConfig;

@ConfigurationProperties(prefix = "aktoer")
public class AktørConfig extends AbstractConfig {
    private static final String AKTØR = "v1/identer";

    private final URI baseUri;

    public AktørConfig(URI baseUri) {
        this.baseUri = baseUri;
    }

    public URI aktørUri() {
        return uri(baseUri, AKTØR);
    }

    @Override
    public URI pingURI() {
        return baseUri;
    }

}
