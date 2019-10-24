package no.nav.foreldrepenger.historikk.tjenester.aktør;

import java.net.URI;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.boot.context.properties.bind.DefaultValue;

import no.nav.foreldrepenger.historikk.http.AbstractConfig;

@ConfigurationProperties(prefix = "aktoer")
@ConstructorBinding
public class AktørConfig extends AbstractConfig {
    private static final String AKTØR = "oppslag/aktor";

    private static final String DEFAULT_BASE_URI = "https://app-q1.adeo.no/aktoerregister/";

    private final URI baseUri;

    public AktørConfig(@DefaultValue({ DEFAULT_BASE_URI }) URI baseUri) {
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
