package no.nav.foreldrepenger.historikk.tjenester.minidialog;

public enum FagsakYtelseType {
    ENGANGSSTONAD("ES"),
    FORELDREPENGER("FP"),
    SVANGERSKAPSPENGER("SVP"),
    UDEFINERT("-");

    private final String navn;

    private FagsakYtelseType(String navn) {
        this.navn = navn;
    }

}
