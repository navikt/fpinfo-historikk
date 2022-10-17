package no.nav.foreldrepenger.historikk.tjenester.dokumentarkiv;

import no.nav.foreldrepenger.historikk.http.AbstractConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

import static no.nav.foreldrepenger.historikk.tjenester.dokumentarkiv.ArkivOppslagJournalposter.ArkivOppslagJournalpost.ArkivOppslagDokumentInfo.ArkivOppslagDokumentVariant.ArkivOppslagDokumentVariantFormat.ARKIV;

@ConfigurationProperties(prefix = "historikk.saf")
public class ArkivOppslagConfig extends AbstractConfig {
    private static final String DEFAULT_PING_PATH = "/actuator/health/liveness";
    private static final String HENT_DOKUMENT_TMPL = "{baseUri}/rest/hentdokument/{journalpostId}/{dokumentInfoId}/{arkivType}";
    private static final String GRAPHQL_TMPL = "{baseUri}/graphql";

    @ConstructorBinding
    public ArkivOppslagConfig(URI baseUri,
                              @DefaultValue(DEFAULT_PING_PATH) String pingPath,
                              @DefaultValue("true") boolean enabled) {
        super(baseUri, pingPath, enabled);
    }

    public URI hentDokumentUri(String journalpostId, String dokumentInfoId) {
        return UriComponentsBuilder.fromUriString(HENT_DOKUMENT_TMPL)
            .buildAndExpand(getBaseUri(), journalpostId, dokumentInfoId, ARKIV.name())
            .toUri();
    }

    public URI dokumenter() {
        return UriComponentsBuilder.fromUriString(GRAPHQL_TMPL)
            .buildAndExpand(getBaseUri())
            .toUri();
    }


}
