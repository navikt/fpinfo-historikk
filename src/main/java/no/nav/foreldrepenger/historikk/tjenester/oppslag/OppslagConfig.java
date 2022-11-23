package no.nav.foreldrepenger.historikk.tjenester.oppslag;

import no.nav.foreldrepenger.historikk.http.AbstractConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.net.URI;

import static no.nav.foreldrepenger.historikk.util.URIUtil.queryParams;
import static no.nav.foreldrepenger.historikk.util.URIUtil.uri;

@ConfigurationProperties(prefix = "historikk.oppslag")
public class OppslagConfig extends AbstractConfig {
    private static final String DEFAULT_MOTTAK_BASE_URI = "http://fpsoknad-mottak/api";
    private static final String DEFAULT_PING_PATH = "/actuator/info";

    private static final String AKTØR = "/oppslag/aktoer";
    private static final String ORGNAVN = "/innsyn/orgnavn";

    @ConstructorBinding
    public OppslagConfig(@DefaultValue(DEFAULT_MOTTAK_BASE_URI) URI baseUri,
                         @DefaultValue(DEFAULT_PING_PATH) String pingPath,
                         @DefaultValue("true") boolean enabled) {
        super(baseUri, pingPath, enabled);
    }

    public URI orgNavnURI(String orgnr) {
        return uri(getBaseUri(), ORGNAVN, queryParams("orgnr", orgnr));
    }

    public URI aktørURI() {
        return uri(getBaseUri(), AKTØR);
    }
}
