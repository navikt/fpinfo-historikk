package no.nav.foreldrepenger.historikk.domain;

import javax.persistence.Embeddable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

@Embeddable
public class AktørId {

    @JsonValue
    private String aktørId;

    public String getAktørId() {
        return aktørId;
    }

    public void setAktørId(String aktørId) {
        this.aktørId = aktørId;
    }

    private AktørId() {
    }

    @JsonCreator
    public AktørId(@JsonProperty("aktørId") String aktørId) {
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