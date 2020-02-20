package no.nav.foreldrepenger.historikk.tjenester.felles;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;

public enum YtelseType {
    ES("engangsstønad"),
    FP("foreldrepenger"),
    SVP("svangerskapspenger"),
    @JsonEnumDefaultValue
    UDEF;

    public String beskrivelse;

    YtelseType() {
        this("udefinert");
    }

    YtelseType(String beskrivelse) {
        this.beskrivelse = beskrivelse;
    }

}
