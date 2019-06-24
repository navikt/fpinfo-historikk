package no.nav.foreldrepenger.historikk.tjenester.journalføring;

public enum DokumentKategori {

    BREV("B"),
    VEDTAKSBREV("VB"),
    INFOBREV("IB"),
    ELEKTRONISK_SKJEMA("ES"),
    ELEKTRONISK_DIALOG("ELEKTRONISK_DIALOG");

    private final String kategori;

    private DokumentKategori(String kategori) {
        this.kategori = kategori;
    }

    public String getKategori() {
        return kategori;
    }
}
