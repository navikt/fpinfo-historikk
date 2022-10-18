package no.nav.foreldrepenger.historikk.tjenester.dokumentarkiv;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.core.JsonProcessingException;
import no.nav.boot.conditionals.ConditionalOnNotProd;
import no.nav.foreldrepenger.historikk.config.JacksonConfiguration;
import no.nav.foreldrepenger.historikk.http.AbstractRestConnection;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestOperations;

import java.net.URI;
import java.util.Map;

@ConditionalOnNotProd
@Component
public class ArkivConnection extends AbstractRestConnection {

    private final ArkivOppslagConfig cfg;


    protected ArkivConnection(RestOperations restOperations, ArkivOppslagConfig config) {
        super(restOperations, config);
        this.cfg = config;
    }

    @Override
    public URI pingEndpoint() {
        return null;
    }

    public byte[] hentDok(String journalpostId, String dokumentInfoId) {
        var uri = cfg.hentDokumentUri(journalpostId, dokumentInfoId);
        return getForObject(uri, byte[].class);
    }

    public ArkivOppslagJournalposter journalposter(String ident) {
        if (ident == null) {
            throw new IllegalArgumentException("Mangler ident");
        }
        var requestBody = new Query(query(), Map.of("ident", ident));
        var wrappedResponse = postForEntity(cfg.dokumenter(), requestBody, DataWrapped.class);
        return wrappedResponse.data().journalposter();
    }

    private static String query() {
        return """
            query dokumentoversiktSelvbetjening($ident: String!) {
              dokumentoversiktSelvbetjening(ident: $ident, tema: [FOR]) {
                journalposter {
                  journalpostId
                  journalposttype
                  journalstatus
                  tittel
                  eksternReferanseId
                  relevanteDatoer {
                    dato
                    datotype
                  }
                  sak {
                    fagsakId
                    fagsaksystem
                    sakstype
                  }
                  dokumenter {
                    dokumentInfoId
                    brevkode
                    tittel
                    dokumentvarianter {
                      variantformat
                      filtype
                      brukerHarTilgang
                    }
                  }
                }
              }
            }""".stripIndent();
    }
    private record Query(String query, Map<String, String> variables) { }
    private record DataWrapped(Wrapped data) { };
    private record Wrapped(@JsonAlias("dokumentOversiktselvbetjening") ArkivOppslagJournalposter journalposter) { }

}
