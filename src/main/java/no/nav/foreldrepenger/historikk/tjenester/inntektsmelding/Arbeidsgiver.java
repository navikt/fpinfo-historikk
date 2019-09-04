package no.nav.foreldrepenger.historikk.tjenester.inntektsmelding;

import javax.persistence.Embeddable;
import javax.validation.Valid;

import no.nav.foreldrepenger.historikk.util.annoteringer.Orgnr;

@Valid
@Embeddable
public class Arbeidsgiver {
    private final String navn;
    @Orgnr
    private final String orgnummer;

    public Arbeidsgiver(String navn, String orgnummer) {
        this.navn = navn;
        this.orgnummer = orgnummer;
    }

    public String getNavn() {
        return navn;
    }

    public String getOrgnummer() {
        return orgnummer;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[navn=" + navn + ", orgnummer=" + orgnummer + "]";
    }
}
