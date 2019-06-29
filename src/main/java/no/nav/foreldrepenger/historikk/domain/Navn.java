package no.nav.foreldrepenger.historikk.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.google.common.base.Joiner;

@JsonPropertyOrder({ "fornavn", "mellomnavn", "etternavn" })
public class Navn {

    private final String fornavn;
    private final String mellomnavn;
    private final String etternavn;
    private final Kjønn kjønn;

    @JsonCreator
    public Navn(@JsonProperty("fornavn") String fornavn, @JsonProperty("mellomnavn") String mellomnavn,
            @JsonProperty("etternavn") String etternavn, @JsonProperty("kjønn") Kjønn kjønn) {
        this.fornavn = fornavn;
        this.mellomnavn = mellomnavn;
        this.etternavn = etternavn;
        this.kjønn = kjønn;
    }

    public String getFornavn() {
        return fornavn;
    }

    public String getMellomnavn() {
        return mellomnavn;
    }

    public String getEtternavn() {
        return etternavn;
    }

    public Kjønn getKjønn() {
        return kjønn;
    }

    public String navn() {
        return Joiner.on(' ').useForNull("").join(fornavn, mellomnavn, etternavn);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[fornavn=" + fornavn + ", mellomnavn=" + mellomnavn + ", etternavn="
                + etternavn + ", kjønn=" + kjønn + "]";
    }

}
