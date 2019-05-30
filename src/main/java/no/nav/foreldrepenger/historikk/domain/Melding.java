package no.nav.foreldrepenger.historikk.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Melding {

    private final int id;
    private final AktørId aktørId;
    private final String melding;

    public Melding(AktørId aktørId, String melding) {
        this(0, aktørId, melding);
    }

    @JsonCreator
    public Melding(@JsonProperty("id") int id, @JsonProperty("aktørId") AktørId aktørId,
            @JsonProperty("melding") String melding) {
        this.id = id;
        this.aktørId = aktørId;
        this.melding = melding;
    }

    public AktørId getAktørId() {
        return aktørId;
    }

    public String getMelding() {
        return melding;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [id=" + id + ", aktørId=" + aktørId + ", melding=" + melding + "]";
    }
}
