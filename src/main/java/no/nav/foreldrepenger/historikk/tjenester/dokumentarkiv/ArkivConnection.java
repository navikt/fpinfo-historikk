package no.nav.foreldrepenger.historikk.tjenester.dokumentarkiv;

import com.fasterxml.jackson.annotation.JsonAlias;
import no.nav.boot.conditionals.ConditionalOnNotProd;
import no.nav.foreldrepenger.historikk.http.AbstractRestConnection;
import no.nav.foreldrepenger.historikk.tjenester.dokumentarkiv.ArkivOppslagJournalposter.ArkivOppslagJournalpost.ArkivOppslagDokumentInfo.ArkivOppslagDokumentVariant;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestOperations;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static no.nav.foreldrepenger.historikk.tjenester.dokumentarkiv.ArkivOppslagJournalposter.ArkivOppslagJournalpost.ArkivOppslagDokumentInfo.ArkivOppslagDokumentVariant.ArkivOppslagDokumentFiltype.*;

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

    public List<ArkivDokument> journalposter(String ident) {
        if (ident == null) {
            throw new IllegalArgumentException("Mangler ident");
        }
        var requestBody = new Query(query(), Map.of("ident", ident));
        var wrappedResponse = postForEntity(cfg.dokumenter(), requestBody, DataWrapped.class);
        return map(wrappedResponse.data().journalposter());
    }

    private static List<ArkivDokument> map(ArkivOppslagJournalposter journalposter) {
        return journalposter.journalposter().stream()
            .flatMap(jp -> jp.dokumenter().stream()
                .filter(dok -> dok.dokumentVarianter().stream()
                    .filter(dv -> dv.filtype().equals(PDF))
                    .findFirst()
                    .map(ArkivOppslagDokumentVariant::brukerHarTilgang)
                    .orElse(false))
                .map(dok -> new ArkivDokument(
                    fra(jp.journalpostType()),
                    opprettetDato(jp.relevanteDatoer()),
                    jp.sak().flatMap(s -> s.fagsakId()).orElse(null),
                    dok.tittel().orElse("Uten tittel"),
                    jp.journalpostId(),
                    url(jp.journalpostId(), dok.dokumentInfoId()))
                ))
            .collect(Collectors.toList());
    }

    private static URI url(String journalpostId, String dokumentId) {
        return UriComponentsBuilder
            .fromUriString("https://foreldrepengesoknad-api.dev.nav.no/rest")
            .pathSegment("dokument", "hent-dokument", journalpostId, dokumentId)
            .build().toUri();
    }

    private static LocalDateTime opprettetDato(List<ArkivOppslagJournalposter.ArkivOppslagJournalpost.ArkivOppslagRelevantDato> datoer) {
        return datoer.stream()
            .filter(d -> d.datoType() == ArkivOppslagJournalposter.ArkivOppslagJournalpost.ArkivOppslagRelevantDato.ArkivOppslagDatoType.DATO_OPPRETTET)
            .map(ArkivOppslagJournalposter.ArkivOppslagJournalpost.ArkivOppslagRelevantDato::dato)
            .findFirst().orElseThrow();
    }

    private static ArkivDokument.DokumentType fra(ArkivOppslagJournalposter.ArkivOppslagJournalpost.ArkivOppslagJournalpostType type) {
        return switch (type) {
            case U -> ArkivDokument.DokumentType.UTGÅENDE_DOKUMENT;
            case I -> ArkivDokument.DokumentType.INNGÅENDE_DOKUMENT;
            default -> throw new IllegalArgumentException("Fikk uventet verdi " + type);
        };
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
