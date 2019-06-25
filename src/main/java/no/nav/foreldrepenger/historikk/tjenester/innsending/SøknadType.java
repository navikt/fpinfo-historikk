package no.nav.foreldrepenger.historikk.tjenester.innsending;

import no.nav.foreldrepenger.historikk.tjenester.journalføring.BehandlingTema;

public enum SøknadType {
    INITIELL_FORELDREPENGER, ETTERSENDING_FORELDREPENGER, ENDRING_FORELDREPENGER,
    INITIELL_ENGANGSSTØNAD, ETTERSENDING_ENGANGSSTØNAD,
    INITIELL_ENGANGSSTØNAD_DOKMOT, INITIELL_SVANGERSKAPSPENGER,
    ETTERSENDING_SVANGERSKAPSPENGER;

    public boolean erEttersending() {
        return this.equals(ETTERSENDING_ENGANGSSTØNAD) || this.equals(ETTERSENDING_FORELDREPENGER)
                || this.equals(ETTERSENDING_SVANGERSKAPSPENGER);
    }

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

    public BehandlingTema tema() {
        if (erSvangerskapspenger()) {
            return BehandlingTema.FORELDRE_OG_SVANGERSKAPSPENGER;
        }
        if (erEngangsstønad()) {
            return BehandlingTema.ENGANGSSTØNAD;
        }
        if (erForeldrepenger()) {
            return BehandlingTema.FORELDREPENGER;
        }
        return BehandlingTema.FORELDREPENGER;
    }

}
