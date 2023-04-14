package no.nav.foreldrepenger.historikk.tjenester.oppslag;

import no.nav.foreldrepenger.historikk.http.AbstractConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Map;

import static no.nav.foreldrepenger.common.util.StringUtil.taint;

@ConfigurationProperties(prefix = "historikk.oppslag")
public class OppslagConfig extends AbstractConfig {
    private static final String DEFAULT_MOTTAK_BASE_URI = "http://fpsoknad-mottak/api";
    private static final String DEFAULT_PING_PATH = "/actuator/info";
    private static final String ORGNAVN = "/innsyn/orgnavn";

    private static final String AKTØR_TEMPLATE = "/oppslag/aktoer";
    private static final String ORGNAVN_TEMPLATE = "/innsyn/orgnavn?orgnr={orgnummer}";

    public OppslagConfig(@DefaultValue(DEFAULT_MOTTAK_BASE_URI) URI baseUri,
                         @DefaultValue(DEFAULT_PING_PATH) String pingPath,
                         @DefaultValue("true") boolean enabled) {
        super(baseUri, pingPath, enabled);
    }

    public URI orgNavnURI(String orgnr) {
        return UriComponentsBuilder
            .fromUri(getBaseUri())
            .path(ORGNAVN)
            .queryParam("orgnr", "{orgnummer}")
            .buildAndExpand(Map.of("orgnummer", taint(orgnr)))
            .toUri();
    }

    public String aktørPath() {
        return AKTØR_TEMPLATE;
    }

    public String orgnavnPathTemplate() {
        return ORGNAVN_TEMPLATE;
    }
}
