package no.nav.foreldrepenger.historikk.tjenester.inntektsmelding;

import javax.validation.Valid;

import no.nav.foreldrepenger.historikk.util.annoteringer.Orgnr;

@Valid
public class ArbeidsgiverInnslag {
    @Orgnr
    private final String orgnr;

    private final String navn;

    public ArbeidsgiverInnslag(String orgnr, String navn) {
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
