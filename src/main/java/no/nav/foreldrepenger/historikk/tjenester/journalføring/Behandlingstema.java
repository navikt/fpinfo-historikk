package no.nav.foreldrepenger.historikk.tjenester.journalføring;

public enum Behandlingstema {
    FORELDREPENGER_VED_FØDSEL("ab0047"),
    FORELDRE_OG_SVANGERSKAPSPENGER("ab0273"),
    FORELDREPENGER_VED_ADOPSJON("ab0072"),
    FORELDEPENGER("ab0326"),
    ENGANGSSTØNAD("ab0327");

    private final String tema;

    private Behandlingstema(String tema) {
        this.tema = tema;
    }

    public String getTema() {
        return tema;
    }

}
