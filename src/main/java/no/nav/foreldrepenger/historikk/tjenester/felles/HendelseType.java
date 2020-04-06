package no.nav.foreldrepenger.historikk.tjenester.felles;

public enum HendelseType {

    TILBAKEKREVING_SPM,
    TILBAKEKREVING_SVAR,
    TILBAKEKREVING_FATTET_VEDTAK,
    TILBAKEKREVING_SPM_LUKKET,
    TILBAKEKREVING_HENLAGT,
    VEDTAK,
    INNTEKTSMELDING_NY,
    INNTEKTSMELDING_ENDRING,
    INITIELL_ENGANGSSTØNAD("søknad om engangsstønad"),
    INITIELL_FORELDREPENGER("søknad om foreldrepenger"),
    INITIELL_SVANGERSKAPSPENGER("søknad om svangerskapspenger"),
    ETTERSENDING_FORELDREPENGER("ettersending til søknad om foreldrepenger"),
    ETTERSENDING_ENGANGSSTØNAD("ettersending til søknad om engangsstønad"),
    ETTERSENDING_SVANGERSKAPSPENGER("ettersending til søknad om svangerskapspenger"),
    ENDRING_FORELDREPENGER("endring av søknad om foreldrepenger"),
    ENDRING_SVANGERSKAPSPENGER("endring av søknad om svangerskapspenger"),
    UKJENT,
    FORDELING;

    private HendelseType() {
        this(null);
    }

    private HendelseType(String beskrivelse) {
        this.beskrivelse = beskrivelse;
    }

    public final String beskrivelse;

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

    public boolean erForeldrepenger() {
        return this.equals(INITIELL_FORELDREPENGER) ||
                this.equals(ETTERSENDING_FORELDREPENGER) ||
                this.equals(ENDRING_FORELDREPENGER);
    }

    public boolean erEngangsstønad() {
        return this.equals(ETTERSENDING_ENGANGSSTØNAD) ||
                this.equals(INITIELL_ENGANGSSTØNAD);
    }

    public boolean erSvangerskapspenger() {
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

}
