package no.nav.foreldrepenger.historikk.domain;

import static no.nav.foreldrepenger.historikk.util.StringUtil.mask;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

public class Fødselsnummer {

    @JsonValue
    private final String fnr;

    @JsonCreator
    public Fødselsnummer(@JsonProperty("fnr") String fnr) {
        this.fnr = fnr;
    }

    public static Fødselsnummer valueOf(String fnr) {
        return new Fødselsnummer(fnr);
    }

    public String getFnr() {
        return fnr;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [fnr=" + mask(fnr) + "]";
    }
}
