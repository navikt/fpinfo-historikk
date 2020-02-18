package no.nav.foreldrepenger.historikk.tjenester.felles;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;

public enum YtelseType {
    ES,
    FP,
    SVP,
    @JsonEnumDefaultValue
    UDEF

}
