package no.nav.foreldrepenger.historikk.meldinger.event;

public enum SøknadType {
    INITIELL_FORELDREPENGER, ETTERSENDING_FORELDREPENGER, ENDRING_FORELDREPENGER,
    INITIELL_ENGANGSSTØNAD, ETTERSENDING_ENGANGSSTØNAD,
    INITIELL_ENGANGSSTØNAD_DOKMOT, INITIELL_SVANGERSKAPSPENGER,
    ETTERSENDING_SVANGERSKAPSPENGER, UKJENT;

    private boolean erForeldrepenger() {
        return this.equals(INITIELL_FORELDREPENGER)
                || this.equals(ENDRING_FORELDREPENGER)
                || this.equals(ETTERSENDING_FORELDREPENGER);
    }

    private boolean erEngangsstønad() {
        return this.equals(INITIELL_ENGANGSSTØNAD)
                || this.equals(ETTERSENDING_ENGANGSSTØNAD)
                || this.equals(INITIELL_ENGANGSSTØNAD_DOKMOT);
    }

    private boolean erSvangerskapspenger() {
        return this.equals(INITIELL_SVANGERSKAPSPENGER)
                || this.equals(ETTERSENDING_SVANGERSKAPSPENGER);
    }

    public boolean erUkjent() {
        return UKJENT.equals(this);
    }

}
