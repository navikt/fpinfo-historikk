package no.nav.foreldrepenger.historikk.tjenester.dokumentarkiv;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import no.nav.foreldrepenger.historikk.tjenester.dokumentarkiv.ArkivOppslagJournalposter.ArkivOppslagJournalpost;
import no.nav.foreldrepenger.historikk.tjenester.dokumentarkiv.ArkivOppslagJournalposter.ArkivOppslagJournalpost.ArkivOppslagDokumentInfo;
import no.nav.foreldrepenger.historikk.tjenester.dokumentarkiv.ArkivOppslagJournalposter.ArkivOppslagJournalpost.ArkivOppslagDokumentInfo.ArkivOppslagDokumentVariant;
import no.nav.foreldrepenger.historikk.tjenester.dokumentarkiv.ArkivOppslagJournalposter.ArkivOppslagJournalpost.ArkivOppslagJournalpostType;
import no.nav.foreldrepenger.historikk.tjenester.dokumentarkiv.ArkivOppslagJournalposter.ArkivOppslagJournalpost.ArkivOppslagSak;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static no.nav.foreldrepenger.historikk.tjenester.dokumentarkiv.ArkivOppslagJournalposter.ArkivOppslagJournalpost.ArkivOppslagDokumentInfo.ArkivOppslagDokumentVariant.ArkivOppslagDokumentFiltype.*;
import static no.nav.foreldrepenger.historikk.tjenester.dokumentarkiv.ArkivOppslagJournalposter.ArkivOppslagJournalpost.ArkivOppslagDokumentInfo.ArkivOppslagDokumentVariant.ArkivOppslagDokumentVariantFormat.ARKIV;

@Component
public class ArkivConnection {

    public static final String SAF = "SAF";
    private static final Logger LOG = LoggerFactory.getLogger(ArkivConnection.class);


    private final ArkivOppslagConfig cfg;
    private final WebClient safClient;


    protected ArkivConnection(ArkivOppslagConfig config,
                              @Qualifier(SAF) WebClient safClient) {
        this.cfg = config;
        this.safClient = safClient;
    }

    public byte[] hentDok(String journalpostId, String dokumentInfoId) {
        return safClient.get()
                        .uri(cfg.hentDokumentTemplate(), journalpostId, dokumentInfoId, ARKIV.name())
                        .retrieve()
                        .bodyToMono(byte[].class)
                        .block();
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
            .map(this::map)
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
    private List<ArkivDokument> map(ArkivOppslagJournalposter journalposter) {
        return journalposter.journalposter().stream()
            .peek(jp -> {
                var systemUliktFS36 = jp.sak().flatMap(ArkivOppslagSak::fagsaksystem)
                    .stream().anyMatch(sys -> !"FS36".equalsIgnoreCase(sys));
                if (systemUliktFS36) {
                    LOG.info("ArkivConnection: får journalpost med system ulikt FS36");
                }
            })
//            .filter(jp -> {
//                // Sjekker om journalpost er opprettet av fpsak med venner
//                return jp.sak()
//                    .flatMap(ArkivOppslagSak::fagsaksystem)
//                    .map("FS36"::equals)
//                    .orElse(false);
//            })
            .filter(jp -> jp.journalpostType() != ArkivOppslagJournalpostType.N)
            .flatMap(jp -> jp.dokumenter().stream()
                .filter(dok -> dok.dokumentVarianter().stream()
                    .filter(dv -> dv.filtype().equals(PDF))
                    .findFirst()
                    .map(ArkivOppslagDokumentVariant::brukerHarTilgang)
                    .orElse(false))
                .map(dok -> tilArkivDokument(jp, dok)
                ))
            .collect(Collectors.toList());
    }

    @NotNull
    private ArkivDokument tilArkivDokument(ArkivOppslagJournalpost jp, ArkivOppslagDokumentInfo dok) {
        var saksnummer = jp.sak().flatMap(ArkivOppslagSak::fagsakId).orElse(null);
        var builder = new ArkivDokument.ArkivDokumentBuilder();
        builder.journalpost(jp.journalpostId());
        builder.type(dokumentType(jp.journalpostType()));
        builder.mottatt(opprettetDato(jp.relevanteDatoer()));
        builder.saksnummer(saksnummer);
        builder.tittel(dok.tittel().orElse("Uten tittel"));
        builder.brevkode(dok.brevkode().orElse(null));
        builder.url(url(jp.journalpostId(), dok.dokumentInfoId()));
        return builder.build();
    }

    private URI url(String journalpostId, String dokumentId) {
        return UriComponentsBuilder
            .fromUriString(cfg.getApiBaseUri())
            .pathSegment("dokument", "hent-dokument", journalpostId, dokumentId)
            .build().toUri();
    }

    private static LocalDateTime opprettetDato(List<ArkivOppslagJournalpost.ArkivOppslagRelevantDato> datoer) {
        return datoer.stream()
            .filter(d -> d.datoType() == ArkivOppslagJournalpost.ArkivOppslagRelevantDato.ArkivOppslagDatoType.DATO_OPPRETTET)
            .map(ArkivOppslagJournalpost.ArkivOppslagRelevantDato::dato)
            .findFirst().orElseThrow();
    }

    private static ArkivDokument.DokumentType dokumentType(ArkivOppslagJournalpostType type) {
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
