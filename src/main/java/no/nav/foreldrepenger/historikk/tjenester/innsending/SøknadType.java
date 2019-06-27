package no.nav.foreldrepenger.historikk.tjenester.innsending;

import no.nav.foreldrepenger.historikk.tjenester.journalføring.BehandlingTema;

public enum SøknadType {
    INITIELL_FORELDREPENGER, ETTERSENDING_FORELDREPENGER,
    ETTERSENDING_ENGANGSSTØNAD, INITIELL_SVANGERSKAPSPENGER,
    ETTERSENDING_SVANGERSKAPSPENGER;

    public boolean erEttersending() {
        return this.equals(ETTERSENDING_ENGANGSSTØNAD) || this.equals(ETTERSENDING_FORELDREPENGER)
                || this.equals(ETTERSENDING_SVANGERSKAPSPENGER);
    }

    private boolean erForeldrepenger() {
        return this.equals(INITIELL_FORELDREPENGER)
                || this.equals(ETTERSENDING_FORELDREPENGER);
    }

    private boolean erEngangsstønad() {
        return this.equals(ETTERSENDING_ENGANGSSTØNAD);
    }

    private boolean erSvangerskapspenger() {
        return this.equals(INITIELL_SVANGERSKAPSPENGER)
                || this.equals(ETTERSENDING_SVANGERSKAPSPENGER);
    }

    public String tema() {
        if (erSvangerskapspenger()) {
            return BehandlingTema.FORELDRE_OG_SVANGERSKAPSPENGER.getTema();
        }
        if (erEngangsstønad()) {
            return BehandlingTema.ENGANGSSTØNAD.getTema();
        }
        if (erForeldrepenger()) {
            return BehandlingTema.FORELDREPENGER.getTema();
        }
        return BehandlingTema.FORELDREPENGER.getTema();
    }

}
