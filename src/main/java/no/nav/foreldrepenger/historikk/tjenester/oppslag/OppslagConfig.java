package no.nav.foreldrepenger.historikk.tjenester.oppslag;

import java.net.URI;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.boot.context.properties.bind.DefaultValue;

import no.nav.foreldrepenger.historikk.domain.AktørId;
import no.nav.foreldrepenger.historikk.domain.Fødselsnummer;
import no.nav.foreldrepenger.historikk.http.AbstractConfig;

@ConfigurationProperties(prefix = "historikk.oppslag")
public class OppslagConfig extends AbstractConfig {
    private static final String AKTØR = "oppslag/aktoer";
    private static final String FNR = "oppslag/fnr";

    private static final String NAVN = "oppslag/navnfnr";
    private static final String ORGNAVN = "innsyn/orgnavn";
    private static final String DEFAULT_MOTTAK_BASE_URI = "http://fpsoknad-mottak/api";

    private static final String DEFAULT_PING_PATH = "actuator/info";

    private final URI uri;

    @ConstructorBinding
    public OppslagConfig(@DefaultValue(DEFAULT_MOTTAK_BASE_URI) URI uri,
            @DefaultValue("true") boolean enabled) {
        super(enabled);
        this.uri = uri;
    }

    public URI getMottakUri() {
        return uri;
    }

    @Override
    public URI pingURI() {
        return uri(uri, DEFAULT_PING_PATH);
    }

    public URI personNavnURI(Fødselsnummer fnr) {
        return uri(uri, NAVN, queryParams("fnr", fnr.getFnr()));

    }

    public URI orgNavnURI(String orgnr) {
        return uri(uri, ORGNAVN, queryParams("orgnr", orgnr));
    }

    public URI aktørURI() {
        return uri(uri, AKTØR);
    }

    public URI fnrURI(AktørId aktørId) {
        return uri(uri, FNR, queryParams("aktorId", aktørId.getAktørId()));
    }

}
