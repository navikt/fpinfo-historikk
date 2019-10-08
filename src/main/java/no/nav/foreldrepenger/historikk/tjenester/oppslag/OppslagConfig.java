package no.nav.foreldrepenger.historikk.tjenester.oppslag;

import java.net.URI;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import no.nav.foreldrepenger.historikk.http.AbstractConfig;

@ConfigurationProperties(prefix = "historikk.oppslag")
@Configuration
public class OppslagConfig extends AbstractConfig {
    private static final String AKTØR = "oppslag/aktor";
    private static final String FNR = "oppslag/fnr";

    private static final String NAVN = "person/navn";
    private static final String ARBEID = "arbeidsforhold";
    private static final String ORGNAVN = ARBEID + "/navn";
    private static final URI DEFAULT_BASE_URI = URI.create("http://fpsoknad-oppslag/api");
    private static final String DEFAULT_PING_PATH = "actuator/info";

    public URI aktørURI() {
        return uri(DEFAULT_BASE_URI, AKTØR);
    }

    public URI fnrURI(String aktørId) {
        return uri(DEFAULT_BASE_URI, FNR, queryParams("aktorId", aktørId));
    }

    @Override
    public URI pingURI() {
        return uri(DEFAULT_BASE_URI, DEFAULT_PING_PATH);
    }

    public URI personNavnURI(String fnr) {
        return uri(DEFAULT_BASE_URI, NAVN, queryParams("fnr", fnr));

    }

    public URI orgNavnURI(String orgnr) {
        return uri(DEFAULT_BASE_URI, ORGNAVN, queryParams("orgnr", orgnr));
    }
}
