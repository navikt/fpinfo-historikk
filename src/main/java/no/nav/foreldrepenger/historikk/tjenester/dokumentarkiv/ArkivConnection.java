package no.nav.foreldrepenger.historikk.tjenester.dokumentarkiv;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static no.nav.foreldrepenger.historikk.tjenester.dokumentarkiv.ArkivOppslagJournalposter.ArkivOppslagJournalpost.ArkivOppslagDokumentInfo.ArkivOppslagDokumentVariant.ArkivOppslagDokumentVariantFormat.ARKIV;

@Component
public class ArkivConnection {

    public static final String SAF = "SAF";
    private static final Logger LOG = LoggerFactory.getLogger(ArkivConnection.class);


    private final ArkivOppslagConfig cfg;
    private final WebClient safClient;
    private final ArkivMapper mapper;


    protected ArkivConnection(ArkivOppslagConfig config,
                              ArkivMapper arkivMapper,
                              @Qualifier(SAF) WebClient safClient) {
        this.cfg = config;
        this.mapper = arkivMapper;
        this.safClient = safClient;
    }

    public byte[] hentDok(String journalpostId, String dokumentInfoId) {
        return safClient.get()
                        .uri(cfg.hentDokumentTemplate(), journalpostId, dokumentInfoId, ARKIV.name())
                        .retrieve()
                        .bodyToMono(byte[].class)
                        .block();
    }

    public ResponseEntity<byte[]> hentDokRespons(String journalpostId, String dokumentInfoId) {
        var response = safClient.get()
            .uri(cfg.hentDokumentTemplate(), journalpostId, dokumentInfoId, ARKIV.name())
            .retrieve()
            .toEntity(byte[].class)
            .block();

        var headers = response.getHeaders();
        var contentType = Optional.ofNullable(headers.getFirst(HttpHeaders.CONTENT_TYPE)).orElse(MediaType.APPLICATION_PDF_VALUE);
        var contentDisp = Optional.ofNullable(headers.getFirst(HttpHeaders.CONTENT_DISPOSITION)).orElse("inline; filename=innhold.pdf");
        return ResponseEntity
            .accepted()
            .header(HttpHeaders.CONTENT_TYPE, contentType)
            .header(HttpHeaders.CONTENT_DISPOSITION, contentDisp)
            .body(response.getBody());
    }

    public List<ArkivDokument> journalposter(String ident) {
        if (ident == null) {
            throw new IllegalArgumentException("Mangler ident");
        }
        var requestBody = requestBody(ident);
        var wrappedResponse = safClient.post()
            .uri(cfg.graphqlPathTemplate())
            .bodyValue(requestBody)
            .retrieve()
            .bodyToMono(Respons.class)
            .block();
        handleErrors(wrappedResponse);
        return Optional.ofNullable(wrappedResponse)
            .map(Respons::data)
            .map(JournalposterWrapper::journalposter)
            .map(mapper::map)
            .orElse(Collections.emptyList());
    }

    private void handleErrors(Respons wrappedResponse) {
        if (wrappedResponse == null) {
            LOG.warn("SAF respons er null, dette er uventet");
            return;
        }
        if (wrappedResponse.errors() != null && !wrappedResponse.errors().isEmpty()) {
            var message = wrappedResponse.errors().stream().map(Errors::message).collect(Collectors.joining(","));
            throw new SafException("Safselvbetjening feiler med message: %s " + message);
        }
    }

    private static RequestBody requestBody(String ident) {
        return new RequestBody(query(), Map.of("ident", ident));
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


    private record RequestBody(String query, Map<String, String> variables) { }
    private record Respons(JournalposterWrapper data, List<Errors> errors) { }
    private record JournalposterWrapper(@JsonAlias("dokumentOversiktselvbetjening") ArkivOppslagJournalposter journalposter) { }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private record Errors (String message) {}

    class SafException extends RuntimeException {

        SafException(String msg) {
            super(msg);
        }
        SafException(String msg, Object... args) {
            super(format(msg, args));
        }
    }

}
