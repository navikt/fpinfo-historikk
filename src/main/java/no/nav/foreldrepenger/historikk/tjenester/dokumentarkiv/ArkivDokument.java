package no.nav.foreldrepenger.historikk.tjenester.dokumentarkiv;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.net.URI;
import java.time.LocalDateTime;


public record ArkivDokument(DokumentType type,
                            LocalDateTime mottatt,
                            String saksnummer,
                            String tittel,
                            @JsonIgnore
                            Brevkode brevkode,
                            URI url,
                            String journalpost,
                            String dokumentId,
                            boolean erHoveddokument) {

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private DokumentType type;
        private LocalDateTime mottatt;
        private String saksnummer;
        private String tittel;
        private Brevkode brevkode;
        private URI url;
        private String journalpost;
        private String dokumentId;
        private boolean erHoveddokument;

        public Builder type(DokumentType type) {
            this.type = type;
            return this;
        }

        public Builder mottatt(LocalDateTime mottatt) {
            this.mottatt = mottatt;
            return this;
        }

        public Builder saksnummer(String saksnummer) {
            this.saksnummer = saksnummer;
            return this;
        }

        public Builder tittel(String tittel) {
            this.tittel = tittel;
            return this;
        }

        public Builder brevkode(String brevkode) {
            if (brevkode == null) {
                return this;
            }
            this.brevkode = Brevkode.fraKode(brevkode);
            return this;
        }

        public Builder url(URI url) {
            this.url = url;
            return this;
        }

        public Builder journalpost(String journalpost) {
            this.journalpost = journalpost;
            return this;
        }

        public Builder dokumentId(String dokumentId) {
            this.dokumentId = dokumentId;
            return this;
        }

        public Builder erHoveddokument(boolean erHoveddokument) {
            this.erHoveddokument = erHoveddokument;
            return this;
        }

        public ArkivDokument build() {
            return new ArkivDokument(type, mottatt, saksnummer, tittel,
                brevkode, url, journalpost, dokumentId, erHoveddokument);
        }
    }
}
