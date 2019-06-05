package no.nav.foreldrepenger.historikk.domain;

import java.time.LocalDate;

public class Melding {

    private long id;
    private final AktørId aktørId;
    private final String melding;
    private final String saknr;
    private LocalDate dato;
    private LeveranseKanal kanal;
    private LocalDate lest;

    public Melding(AktørId aktørId, String melding, String saksnr) {
        this.aktørId = aktørId;
        this.melding = melding;
        this.saknr = saksnr;
    }

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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

}
