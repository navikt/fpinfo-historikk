package no.nav.foreldrepenger.historikk.tjenester.journalføring;

import java.util.List;

public class Dokument {
    private final String tittel;
    private final String brevkode;
    private final DokumentKategori dokumentKategori;
    private final List<DokumentVariant> dokumentVarianter;

    public Dokument(String tittel, String brevkode, DokumentKategori dokumentKategori,
            List<DokumentVariant> dokumentVarianter) {
        this.tittel = tittel;
        this.brevkode = brevkode;
        this.dokumentKategori = dokumentKategori;
        this.dokumentVarianter = dokumentVarianter;
    }

    public String getTittel() {
        return tittel;
    }

    public String getBrevkode() {
        return brevkode;
    }

    public DokumentKategori getDokumentKategori() {
        return dokumentKategori;
    }

    public List<DokumentVariant> getDokumentVarianter() {
        return dokumentVarianter;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[tittel=" + tittel + ", brevkode=" + brevkode + ", dokumentKategori="
                + dokumentKategori + ", dokumentVarianter=" + dokumentVarianter + "]";
    }

}
