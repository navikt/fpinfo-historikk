package no.nav.foreldrepenger.historikk.tjenester.felles;

import static no.nav.foreldrepenger.historikk.tjenester.journalføring.BehandlingTema.ENGANGSSTØNAD;
import static no.nav.foreldrepenger.historikk.tjenester.journalføring.BehandlingTema.FORELDREPENGER;
import static no.nav.foreldrepenger.historikk.tjenester.journalføring.BehandlingTema.FORELDRE_OG_SVANGERSKAPSPENGER;

public enum HendelseType {
    TILBAKEKREVING_SPM,
    TILBAKEKREVING_SVAR,
    VEDTAK,
    INNTEKTSMELDING,
    INNTEKTSMELDING_ENDRING,
    INITIELL_ENGANGSSTØNAD,
    INITIELL_FORELDREPENGER,
    INITIELL_SVANGERSKAPSPENGER,
    ETTERSENDING_FORELDREPENGER,
    ETTERSENDING_ENGANGSSTØNAD,
    ETTERSENDING_SVANGERSKAPSPENGER,
    ENDRING_FORELDREPENGER,
    ENDRING_SVANGERSKAPSPENGER,
    UKJENT;

    public boolean erMinidialog() {
        return this.equals(TILBAKEKREVING_SPM) ||
                this.equals(TILBAKEKREVING_SVAR);
    }

    public boolean erSøknad() {
        return erEngangsstønad() || erForeldrepenger() || erSvangerskapspenger();
    }

    public boolean erEttersending() {
        return this.equals(ETTERSENDING_ENGANGSSTØNAD) ||
                this.equals(ETTERSENDING_FORELDREPENGER) ||
                this.equals(ETTERSENDING_SVANGERSKAPSPENGER) ||
                this.equals(TILBAKEKREVING_SVAR);
    }

    private boolean erForeldrepenger() {
        return this.equals(INITIELL_FORELDREPENGER) ||
                this.equals(ETTERSENDING_FORELDREPENGER) ||
                this.equals(ENDRING_FORELDREPENGER);
    }

    private boolean erEngangsstønad() {
        return this.equals(ETTERSENDING_ENGANGSSTØNAD) ||
                this.equals(INITIELL_ENGANGSSTØNAD);
    }

    private boolean erSvangerskapspenger() {
        return this.equals(ETTERSENDING_SVANGERSKAPSPENGER) ||
                this.equals(INITIELL_SVANGERSKAPSPENGER) ||
                this.equals(ENDRING_SVANGERSKAPSPENGER);
    }

    public static HendelseType tilHendelse(String hendelse) {
        try {
            return valueOf(hendelse);
        } catch (Exception e) {
            return UKJENT;
        }
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
