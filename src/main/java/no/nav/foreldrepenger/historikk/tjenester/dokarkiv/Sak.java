package no.nav.foreldrepenger.historikk.tjenester.dokarkiv;

import static no.nav.foreldrepenger.historikk.tjenester.dokarkiv.ArkivsakSystem.GSAK;

public class Sak {
    private final String arkivsaksnummer;
    private final ArkivsakSystem arkivsaksystem;

    public Sak(String arkivsaksnummer) {
        this(arkivsaksnummer, GSAK);
    }

    private Sak(String arkivsaksnummer, ArkivsakSystem arkivsaksystem) {
        this.arkivsaksnummer = arkivsaksnummer;
        this.arkivsaksystem = arkivsaksystem;
    }

    public String getArkivsaksnummer() {
        return arkivsaksnummer;
    }

    public ArkivsakSystem getArkivsaksystem() {
        return arkivsaksystem;
    }

    @Override
    public String toString() {
        return ""
                + getClass().getSimpleName() + "[arkivsaksnummer=" + arkivsaksnummer + ", arkivsaksystem="
                + arkivsaksystem + "]";
    }
}
