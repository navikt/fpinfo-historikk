package no.nav.foreldrepenger.historikk.meldinger.event;

public enum SøknadType {
    INITIELL_FORELDREPENGER, ETTERSENDING_FORELDREPENGER, ENDRING_FORELDREPENGER,
    INITIELL_ENGANGSSTØNAD, ETTERSENDING_ENGANGSSTØNAD,
    INITIELL_ENGANGSSTØNAD_DOKMOT, INITIELL_SVANGERSKAPSPENGER,
    ETTERSENDING_SVANGERSKAPSPENGER, UKJENT;

    public boolean erUkjent() {
        return UKJENT.equals(this);
    }

    public boolean erEttersending() {
        return this.equals(ETTERSENDING_ENGANGSSTØNAD) || this.equals(ETTERSENDING_FORELDREPENGER)
                || this.equals(ETTERSENDING_SVANGERSKAPSPENGER);
    }

}
