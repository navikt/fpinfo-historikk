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
    AVSLAG, // avslagsbrev
    OPPHOR, // opphør
    INNSVP, // innvilgelse svp
    AVSLFP, // avslag foreldrepenger

    INNVFP,
    POSVED,
    INNLYS, // etterlys im fritekst
    INNHEN, // innhent dokumentasjon
    UKJENT;

    public boolean erVedtaksbrev() {
        return Set.of(ANUFOR, OPPFOR, AVSFOR, INNVES, OPPSVP, AVSLAG, INNSVP, OPPHOR, INVFOR, INNVFP, INVSVP, AVSLFP, POSVED, AVSSVP, AVSLES).contains(this);
    }

    public boolean erInnhentOpplysningerbrev() {
        return Set.of(INNOPP, INNHEN).contains(this);
    }

    public boolean erEtterlysInntektsmeldingbrev() {
        return Set.of(INNLYS, ELYSIM).contains(this);
    }
}
