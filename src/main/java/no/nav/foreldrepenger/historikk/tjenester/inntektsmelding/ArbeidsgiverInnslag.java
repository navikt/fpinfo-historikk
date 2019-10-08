package no.nav.foreldrepenger.historikk.tjenester.inntektsmelding;

import javax.persistence.Embeddable;
import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import no.nav.foreldrepenger.historikk.util.annoteringer.Orgnr;

@Valid
@Embeddable
public class ArbeidsgiverInnslag {
    @Orgnr
    private final String orgnr;

    private final String navn;

    @JsonCreator
    public ArbeidsgiverInnslag(@JsonProperty("orgnr") String orgnr, @JsonProperty("navn") String navn) {
        this.orgnr = orgnr;
        this.navn = navn;
    }

    public String getNavn() {
        return navn;
    }

    public String getOrgnr() {
        return orgnr;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[orgnr=" + orgnr + ", navn=" + navn + "]";
    }

}
