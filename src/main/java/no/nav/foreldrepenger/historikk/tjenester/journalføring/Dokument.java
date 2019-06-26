package no.nav.foreldrepenger.historikk.tjenester.journalføring;

import java.util.Arrays;
import java.util.List;

public class Dokument {
    private final String tittel;
    private final DokumentKategori dokumentKategori;
    private final List<DokumentVariant> dokumentVarianter;

    public Dokument(String tittel, DokumentVariant... dokumentVarianter) {
        this.tittel = tittel;
        this.dokumentKategori = DokumentKategori.ELEKTRONISK_DIALOG;
        this.dokumentVarianter = Arrays.asList(dokumentVarianter);
    }

    public String getTittel() {
        return tittel;
    }

    public DokumentKategori getDokumentKategori() {
        return dokumentKategori;
    }

    public List<DokumentVariant> getDokumentVarianter() {
        return dokumentVarianter;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[tittel=" + tittel + ", dokumentKategori="
                + dokumentKategori + ", dokumentVarianter=" + dokumentVarianter + "]";
    }

}
