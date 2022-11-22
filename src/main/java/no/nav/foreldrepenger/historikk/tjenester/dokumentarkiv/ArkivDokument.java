package no.nav.foreldrepenger.historikk.tjenester.dokumentarkiv;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.net.URI;
import java.time.LocalDateTime;

public record ArkivDokument(
    DokumentType type,
    LocalDateTime mottatt,
    String saksnummer,
    String tittel,
    @JsonIgnore
    String journalpost,
    URI url
) {
    public enum DokumentType { UTGÅENDE_DOKUMENT, INNGÅENDE_DOKUMENT }
}
