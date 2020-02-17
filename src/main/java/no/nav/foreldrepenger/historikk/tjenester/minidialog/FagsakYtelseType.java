package no.nav.foreldrepenger.historikk.tjenester.minidialog;

import com.fasterxml.jackson.annotation.JsonValue;

public enum FagsakYtelseType {
    ENGANGSSTONAD("ES"),
    FORELDREPENGER("FP"),
    SVANGERSKAPSPENGER("SVP"),
    UDEFINERT("-");

    @JsonValue
    private final String navn;

    private FagsakYtelseType(String navn) {
        this.navn = navn;
    }

}
