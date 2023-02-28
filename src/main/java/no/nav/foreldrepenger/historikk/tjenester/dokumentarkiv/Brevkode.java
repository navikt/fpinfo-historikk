package no.nav.foreldrepenger.historikk.tjenester.dokumentarkiv;

import java.util.Set;

public enum Brevkode {
    INNVES, // Vedtak om innvilgelse av engangsstønad
    AVSSVP, // Avslagsbrev svangerskapspenger
    INVFOR, // Innvilgelsesbrev Foreldrepenger
    AVSLES, // Avslag engangsstønad
    OPPFOR, // Opphør Foreldrepenger
    INVSVP, // Innvilgelsesbrev svangerskapspenger
    ANUFOR, // Annullering av Foreldrepenger
    AVSFOR, // Avslagsbrev Foreldrepenger
    OPPSVP, // Opphørsbrev svangerskapspenger
    INNOPP, // Innhent opplysninger
    ELYSIM, // Etterlys inntektsmelding
    UKJENT;

    public boolean erVedtaksbrev() {
        return Set.of(ANUFOR, AVSFOR, OPPSVP, INNVES, AVSSVP, INVFOR, AVSLES, OPPFOR, INVSVP).contains(this);
    }

    public boolean erInnhentOpplysningerbrev() {
        return INNOPP == this;
    }

    public boolean erEtterlysInntektsmeldingbrev() {
        return ELYSIM == this;
    }
}
