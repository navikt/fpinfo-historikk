package no.nav.foreldrepenger.historikk.tjenester.inntektsmelding;

import static no.nav.foreldrepenger.common.util.StringUtil.limitLast;

import jakarta.persistence.Embeddable;
import jakarta.validation.Valid;

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
        return getClass().getSimpleName()
            + "[id=" + limitLast(id, 3)
            + ", navn=" + limitLast(navn, 0)
            + "]";
    }

}
