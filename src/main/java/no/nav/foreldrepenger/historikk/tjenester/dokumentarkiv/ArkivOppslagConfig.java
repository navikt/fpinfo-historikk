package no.nav.foreldrepenger.historikk.tjenester.dokumentarkiv;

import no.nav.foreldrepenger.historikk.http.AbstractConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.net.URI;

@ConfigurationProperties(prefix = "historikk.saf")
public class ArkivOppslagConfig extends AbstractConfig {

    private static final String DEFAULT_PING_PATH = "/actuator/health/liveness";
    private static final String GRAPHQL_PATH_TMPL = "/graphql";
    private static final String HENT_DOKUMENT_PATH_TMPL = "/rest/hentdokument/{journalpostId}/{dokumentInfoId}/{arkivType}";
    private final String apiBaseUri;

    public ArkivOppslagConfig(URI baseUri,
                              String apiBaseUri,
                              @DefaultValue(DEFAULT_PING_PATH) String pingPath,
                              @DefaultValue("true") boolean enabled) {
        super(baseUri, pingPath, enabled);
        this.apiBaseUri = apiBaseUri;
    }

    public String hentDokumentTemplate() {
        return HENT_DOKUMENT_PATH_TMPL;
    }

    public String graphqlPathTemplate() {
        return GRAPHQL_PATH_TMPL;
    }

    public String getApiBaseUri() {
        return apiBaseUri;
    }


}
