package no.nav.foreldrepenger.historikk.tjenester.inntektsmelding;

import javax.persistence.Embeddable;
import javax.validation.Valid;

import no.nav.foreldrepenger.historikk.util.annoteringer.Orgnr;

@Valid
@Embeddable
public class Arbeidsgiver {
    @Orgnr
    private String orgnr;

    private Arbeidsgiver() {

    }

    public Arbeidsgiver(String orgnr) {
        this.orgnr = orgnr;
    }

    public String getOrgnr() {
        return orgnr;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[orgnr=" + orgnr + "]";
    }
}
