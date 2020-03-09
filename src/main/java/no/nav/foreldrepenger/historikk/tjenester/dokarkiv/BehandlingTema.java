package no.nav.foreldrepenger.historikk.tjenester.dokarkiv;

public enum BehandlingTema {
    FORELDREPENGER_VED_FØDSEL("ab0047"),
    FORELDRE_OG_SVANGERSKAPSPENGER("ab0273"),
    FORELDREPENGER_VED_ADOPSJON("ab0072"),
    FORELDREPENGER("ab0326"),
    ENGANGSSTØNAD("ab0327");

    private final String tema;

    private BehandlingTema(String tema) {
        this.tema = tema;
    }

    public String getTema() {
        return tema;
    }

}
