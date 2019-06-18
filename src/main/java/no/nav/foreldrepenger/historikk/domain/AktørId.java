package no.nav.foreldrepenger.historikk.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

public class AktørId {

    @JsonValue
    private String aktørId;

    @JsonCreator
    public AktørId(@JsonProperty("aktørId") String aktørId) {
        this.aktørId = aktørId;
    }

    public String getAktørId() {
        return aktørId;
    }

    public void setAktørId(String aktørId) {
        this.aktørId = aktørId;
    }

    public static AktørId valueOf(String id) {
        return new AktørId(id);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [aktørId=" + aktørId + "]";
    }
}