package no.nav.foreldrepenger.historikk.tjenester.dokumentarkiv;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Set;

@Builder
@Data
public class ArkivDokument {
    private DokumentType type;
    private LocalDateTime mottatt;
    private String saksnummer;
    private String tittel;
    @JsonIgnore
    private Brevkode brevkode;
    private URI url;
    private String journalpost;
    private String dokumentId;
    private boolean hovedDokument;

    public enum DokumentType { UTGÅENDE_DOKUMENT, INNGÅENDE_DOKUMENT }

    public enum Brevkode {
        INNVES, // Vedtak om innvilgelse av engangsstønad
        AVSSVP, // Avslagsbrev svangerskapspenger
        INVFOR, // Innvilgelsesbrev Foreldrepenger
        AVSLES, // Avslag engangsstønad
        OPPFOR, // Opphør Foreldrepenger
        INVSVP, // Innvilgelsesbrev svangerskapspenger
        ANUFOR, // Annullering av Foreldrepenger
        AVSFOR, // Avslagsbrev Foreldrepenger
        OPPSVP, // Opphørsbrev svangerskapspenger
        INNOPP, // Innhent opplysninger
        UKJENT;

        public boolean erVedtaksbrev() {
            return Set.of(ANUFOR, AVSFOR, OPPSVP, INNVES, AVSSVP, INVFOR, AVSLES, OPPFOR, INVSVP).contains(this);
        }

        public boolean erInnhentOpplysningerbrev() {
            return INNOPP == this;
        }
    }

    public static class ArkivDokumentBuilder {
        public ArkivDokumentBuilder brevkode(String brevkode) {
            if (brevkode == null) {
                return this;
            }
            this.brevkode = Arrays.stream(Brevkode.values())
                .filter(kode -> kode.name().equals(brevkode))
                .findFirst()
                .orElse(Brevkode.UKJENT);
            return this;
        }
    }
}
