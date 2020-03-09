package no.nav.foreldrepenger.historikk.tjenester.aktør;

import java.net.URI;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.boot.context.properties.bind.DefaultValue;

import no.nav.foreldrepenger.historikk.http.AbstractConfig;

@ConfigurationProperties(prefix = "historikk.aktoer")
public class AktørConfig extends AbstractConfig {
    private static final String AKTØR = "v1/identer";

    private final URI uri;

    @ConstructorBinding
    public AktørConfig(@DefaultValue("http://oppslag") URI uri, boolean enabled) {
        super(enabled);
        this.uri = uri;
    }

    public URI aktørUri() {
        return uri(uri, AKTØR);
    }

    @Override
    public URI pingURI() {
        return uri;
    }

}
