package no.nav.foreldrepenger.historikk.tjenester.innsending;

import static no.nav.foreldrepenger.historikk.tjenester.journalføring.BehandlingTema.ENGANGSSTØNAD;
import static no.nav.foreldrepenger.historikk.tjenester.journalføring.BehandlingTema.FORELDREPENGER;
import static no.nav.foreldrepenger.historikk.tjenester.journalføring.BehandlingTema.FORELDRE_OG_SVANGERSKAPSPENGER;

public enum SøknadType {
    INITIELL_FORELDREPENGER,
    ETTERSENDING_FORELDREPENGER,
    ETTERSENDING_ENGANGSSTØNAD,
    ETTERSENDING_SVANGERSKAPSPENGER;

    public boolean erEttersending() {
        return this.equals(ETTERSENDING_ENGANGSSTØNAD) ||
                this.equals(ETTERSENDING_FORELDREPENGER) ||
                this.equals(ETTERSENDING_SVANGERSKAPSPENGER);
    }

    private boolean erForeldrepenger() {
        return this.equals(INITIELL_FORELDREPENGER) ||
                this.equals(ETTERSENDING_FORELDREPENGER);
    }

    private boolean erEngangsstønad() {
        return this.equals(ETTERSENDING_ENGANGSSTØNAD);
    }

    private boolean erSvangerskapspenger() {
        return this.equals(ETTERSENDING_SVANGERSKAPSPENGER);
    }

    public String tema() {
        if (erSvangerskapspenger()) {
            return FORELDRE_OG_SVANGERSKAPSPENGER.getTema();
        }
        if (erEngangsstønad()) {
            return ENGANGSSTØNAD.getTema();
        }
        if (erForeldrepenger()) {
            return FORELDREPENGER.getTema();
        }
        return FORELDREPENGER.getTema();
    }

}
