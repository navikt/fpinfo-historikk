package no.nav.foreldrepenger.historikk.tjenester.journalføring;

import static no.nav.foreldrepenger.historikk.tjenester.journalføring.DokumentKategori.ELEKTRONISK_DIALOG;

import java.util.Arrays;
import java.util.List;

public class Dokument {
    private final String tittel;
    private final DokumentKategori dokumentKategori;
    private final List<DokumentVariant> dokumentvarianter;

    private static final String SPØRSMÅL = "Spørsmål fra saksbehandler";

    public Dokument(DokumentVariant... dokumentvarianter) {
        this(SPØRSMÅL, dokumentvarianter);
    }

    public Dokument(String tittel, DokumentVariant... dokumentvarianter) {
        this.tittel = tittel;
        this.dokumentKategori = ELEKTRONISK_DIALOG;
        this.dokumentvarianter = Arrays.asList(dokumentvarianter);
    }

    public String getTittel() {
        return tittel;
    }

    public DokumentKategori getDokumentKategori() {
        return dokumentKategori;
    }

    public List<DokumentVariant> getDokumentvarianter() {
        return dokumentvarianter;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[tittel=" + tittel + ", dokumentKategori="
                + dokumentKategori + ", dokumentvarianter=" + dokumentvarianter + "]";
    }

}
