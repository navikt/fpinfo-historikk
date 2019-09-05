package no.nav.foreldrepenger.historikk.tjenester.inntektsmelding;

import javax.persistence.Embeddable;
import javax.validation.Valid;

import no.nav.foreldrepenger.historikk.util.annoteringer.Orgnr;

@Valid
@Embeddable
public class Arbeidsgiver {
    private String navn;
    @Orgnr
    private String orgnr;

    private Arbeidsgiver() {

    }

    public Arbeidsgiver(String navn, String orgnr) {
        this.navn = navn;
        this.orgnr = orgnr;
    }

    public String getNavn() {
        return navn;
    }

    public String getOrgnr() {
        return orgnr;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[navn=" + navn + ", orgnr=" + orgnr + "]";
    }
}
