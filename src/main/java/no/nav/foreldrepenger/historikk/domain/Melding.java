package no.nav.foreldrepenger.historikk.domain;

import java.time.LocalDate;

public class Melding {

    private final AktørId aktørId;
    private final String melding;
    private final String saknr;
    private LocalDate dato;
    private LeveranseKanal kanal;
    private LocalDate lest;

    public LocalDate getLest() {
        return lest;
    }

    public void setLest(LocalDate lest) {
        this.lest = lest;
    }

    public LeveranseKanal getKanal() {
        return kanal;
    }

    public void setKanal(LeveranseKanal kanal) {
        this.kanal = kanal;
    }

    public Melding(AktørId aktørId, String melding, String saksnr) {
        this.aktørId = aktørId;
        this.melding = melding;
        this.saknr = saksnr;
    }

    public LocalDate getDato() {
        return dato;
    }

    public void setDato(LocalDate dato) {
        this.dato = dato;
    }

    public AktørId getAktørId() {
        return aktørId;
    }

    public String getMelding() {
        return melding;
    }

    public String getSaknr() {
        return saknr;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [aktørId=" + aktørId + ", melding=" + melding + ", saknr=" + saknr
                + ", dato=" + dato + ", kanal=" + kanal + "]";
    }

}
