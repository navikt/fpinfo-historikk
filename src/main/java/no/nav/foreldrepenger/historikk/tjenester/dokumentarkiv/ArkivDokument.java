package no.nav.foreldrepenger.historikk.tjenester.dokumentarkiv;

import java.net.URI;
import java.time.LocalDateTime;

public record ArkivDokument(
    DokumentType type,
    LocalDateTime mottatt,
    String saksnummer,
    String tittel,
    URI url
) {
    enum DokumentType { UTGÅENDE_DOKUMENT, INNGÅENDE_DOKUMENT }
}
