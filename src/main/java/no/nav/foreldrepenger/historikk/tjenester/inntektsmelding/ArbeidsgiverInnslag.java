package no.nav.foreldrepenger.historikk.tjenester.inntektsmelding;

import javax.persistence.Embeddable;
import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

@Valid
@Embeddable
public class ArbeidsgiverInnslag {
    private final String id;

    private final String navn;

    @JsonCreator
    public ArbeidsgiverInnslag(@JsonProperty("id") String id, @JsonProperty("navn") String navn) {
        this.id = id;
        this.navn = navn;
    }

    public String getNavn() {
        return navn;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[id=" + id + ", navn=" + navn + "]";
    }

}
