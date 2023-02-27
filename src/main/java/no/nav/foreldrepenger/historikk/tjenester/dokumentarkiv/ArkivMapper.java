package no.nav.foreldrepenger.historikk.tjenester.dokumentarkiv;

import no.nav.foreldrepenger.historikk.tjenester.dokumentarkiv.ArkivOppslagJournalposter.ArkivOppslagJournalpost;
import no.nav.foreldrepenger.historikk.tjenester.dokumentarkiv.ArkivOppslagJournalposter.ArkivOppslagJournalpost.ArkivOppslagDokumentInfo;
import no.nav.foreldrepenger.historikk.tjenester.dokumentarkiv.ArkivOppslagJournalposter.ArkivOppslagJournalpost.ArkivOppslagSak;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static no.nav.foreldrepenger.historikk.tjenester.dokumentarkiv.ArkivOppslagJournalposter.ArkivOppslagJournalpost.ArkivOppslagDokumentInfo.ArkivOppslagDokumentVariant.ArkivOppslagDokumentFiltype.PDF;
import static no.nav.foreldrepenger.historikk.tjenester.dokumentarkiv.ArkivOppslagJournalposter.ArkivOppslagJournalpost.ArkivOppslagJournalpostType.*;

@Component
public final class ArkivMapper {
    private static final Logger LOG = LoggerFactory.getLogger(ArkivMapper.class);
    private final ArkivOppslagConfig config;

    public ArkivMapper(ArkivOppslagConfig config) {
        this.config = config;
    }

    public List<ArkivDokument> map(ArkivOppslagJournalposter journalposter) {
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
                            .filter(jp -> List.of(I, U).contains(jp.journalpostType()))
                            .flatMap(jp -> {
                                // første dokumentet er hoveddokument
                                var hovedDokument = jp.dokumenter().get(0);
                                return jp.dokumenter().stream()
                                         .filter(dok -> dok.dokumentVarianter().stream()
                                                           .filter(dv -> dv.filtype().equals(PDF))
                                                           .findFirst()
                                                           .map(ArkivOppslagDokumentInfo.ArkivOppslagDokumentVariant::brukerHarTilgang)
                                                           .orElse(false))
                                         .map(dok -> tilArkivDokument(jp, dok, hovedDokument));
                            })
                            .collect(Collectors.toList());
    }

    @NotNull
    private ArkivDokument tilArkivDokument(ArkivOppslagJournalpost jp,
                                           ArkivOppslagDokumentInfo dok,
                                           ArkivOppslagDokumentInfo hovedDokument) {
        var saksnummer = jp.sak().flatMap(ArkivOppslagSak::fagsakId).orElse(null);
        var builder = new ArkivDokument.ArkivDokumentBuilder();
        builder.journalpost(jp.journalpostId());
        builder.dokumentId(dok.dokumentInfoId());
        builder.type(dokumentType(jp.journalpostType()));
        builder.mottatt(opprettetDato(jp.relevanteDatoer()));
        builder.saksnummer(saksnummer);
        builder.tittel(dok.tittel().orElse("Uten tittel"));
        builder.brevkode(dok.brevkode().orElse(null));
        builder.url(url(jp.journalpostId(), dok.dokumentInfoId()));
        builder.erHovedDokument(hovedDokument.equals(dok));
        return builder.build();
    }

    private URI url(String journalpostId, String dokumentId) {
        return UriComponentsBuilder
            .fromUriString(config.getApiBaseUri())
            .pathSegment("dokument", "hent-dokument", journalpostId, dokumentId)
            .build().toUri();
    }

    private static LocalDateTime opprettetDato(List<ArkivOppslagJournalpost.ArkivOppslagRelevantDato> datoer) {
        return datoer.stream()
                     .filter(d -> d.datoType() == ArkivOppslagJournalpost.ArkivOppslagRelevantDato.ArkivOppslagDatoType.DATO_OPPRETTET)
                     .map(ArkivOppslagJournalpost.ArkivOppslagRelevantDato::dato)
                     .findFirst().orElseThrow();
    }

    private static ArkivDokument.DokumentType dokumentType(ArkivOppslagJournalpost.ArkivOppslagJournalpostType type) {
        return switch (type) {
            case U -> ArkivDokument.DokumentType.UTGÅENDE_DOKUMENT;
            case I -> ArkivDokument.DokumentType.INNGÅENDE_DOKUMENT;
            default -> throw new IllegalArgumentException("Fikk uventet verdi " + type);
        };
    }

}
