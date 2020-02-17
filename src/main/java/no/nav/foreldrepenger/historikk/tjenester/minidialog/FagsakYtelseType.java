package no.nav.foreldrepenger.historikk.tjenester.minidialog;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;

public enum FagsakYtelseType {
    ES,
    FP,
    SVP,
    @JsonEnumDefaultValue
    UDEF

}
