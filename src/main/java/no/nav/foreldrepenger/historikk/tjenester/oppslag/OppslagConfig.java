package no.nav.foreldrepenger.historikk.tjenester.oppslag;

import java.net.URI;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

import no.nav.foreldrepenger.historikk.http.AbstractConfig;

@ConfigurationProperties(prefix = "historikk.oppslag")
public class OppslagConfig extends AbstractConfig {
    private static final String AKTØR = "oppslag/aktor";
    private static final String FNR = "oppslag/fnr";

    private static final String NAVN = "person/navn";
    private static final String ARBEID = "arbeidsforhold";
    private static final String ORGNAVN = ARBEID + "/navn";
    private static final String DEFAULT_BASE_URI = "http://fpsoknad-oppslag/api";
    private static final String DEFAULT_PING_PATH = "actuator/info";

    private final URI baseURI;

    public OppslagConfig(@DefaultValue({ DEFAULT_BASE_URI }) URI baseURI) {
        this.baseURI = baseURI;
    }

    public URI aktørURI() {
        return uri(baseURI, AKTØR);
    }

    public URI fnrURI(String aktørId) {
        return uri(baseURI, FNR, queryParams("aktorId", aktørId));
    }

    @Override
    public URI pingURI() {
        return uri(baseURI, DEFAULT_PING_PATH);
    }

    public URI personNavnURI(String fnr) {
        return uri(baseURI, NAVN, queryParams("fnr", fnr));

    }

    public URI orgNavnURI(String orgnr) {
        return uri(baseURI, ORGNAVN, queryParams("orgnr", orgnr));
    }
}
