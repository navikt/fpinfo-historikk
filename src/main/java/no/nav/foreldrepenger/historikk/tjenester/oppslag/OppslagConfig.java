package no.nav.foreldrepenger.historikk.tjenester.oppslag;

import static no.nav.foreldrepenger.historikk.util.URIUtil.queryParams;
import static no.nav.foreldrepenger.historikk.util.URIUtil.uri;

import java.net.URI;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.boot.context.properties.bind.DefaultValue;

import no.nav.foreldrepenger.historikk.domain.AktørId;
import no.nav.foreldrepenger.historikk.domain.Fødselsnummer;
import no.nav.foreldrepenger.historikk.http.AbstractConfig;

@ConfigurationProperties(prefix = "historikk.oppslag")
public class OppslagConfig extends AbstractConfig {
    private static final String DEFAULT_MOTTAK_BASE_URI = "http://fpsoknad-mottak/api";
    private static final String DEFAULT_PING_PATH = "/actuator/info";

    private static final String AKTØR = "/oppslag/aktoer";
    private static final String FNR = "/oppslag/fnr";
    private static final String NAVN = "/oppslag/navnfnr";
    private static final String ORGNAVN = "/innsyn/orgnavn";

    @ConstructorBinding
    public OppslagConfig(@DefaultValue(DEFAULT_MOTTAK_BASE_URI) URI baseUri,
                         @DefaultValue(DEFAULT_PING_PATH) String pingPath,
                         @DefaultValue("true") boolean enabled) {
        super(baseUri, pingPath, enabled);
    }

    public URI personNavnURI(Fødselsnummer fnr) {
        return uri(getBaseUri(), NAVN, queryParams("fnr", fnr.getFnr()));
    }

    public URI orgNavnURI(String orgnr) {
        return uri(getBaseUri(), ORGNAVN, queryParams("orgnr", orgnr));
    }

    public URI aktørURI() {
        return uri(getBaseUri(), AKTØR);
    }

    public URI fnrURI(AktørId aktørId) {
        return uri(getBaseUri(), FNR, queryParams("aktorId", aktørId.getAktørId()));
    }
}
