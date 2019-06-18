package no.nav.foreldrepenger.historikk.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

public class AktørId {

    private final String aktørId;

    @JsonCreator
    public AktørId(@JsonProperty("aktørId") String aktørId) {
        this.aktørId = aktørId;
    }

    @JsonValue
    public String getAktørId() {
        return aktørId;
    }

    public static AktørId valueOf(String aktørId) {
        return new AktørId(aktørId);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [aktørId=" + aktørId + "]";
    }
}