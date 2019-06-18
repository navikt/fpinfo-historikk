package no.nav.foreldrepenger.historikk.domain;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import no.nav.foreldrepenger.historikk.meldinger.event.SøknadType;

public class MinidialogInnslag {

    private long id;
    private final String aktørId;
    private final String melding;
    private final String saknr;
    private LocalDate dato;
    private LeveranseKanal kanal;
    private LocalDate gyldigTil;
    private SøknadType handling;
    private boolean aktiv;

    @JsonCreator
    public MinidialogInnslag(@JsonProperty("aktørId") String aktørId, @JsonProperty("melding") String melding,
            @JsonProperty("saksnr") String saksnr) {
        this.aktørId = aktørId;
        this.melding = melding;
        this.saknr = saksnr;
    }

    public LocalDate getGyldigTil() {
        return gyldigTil;
    }

    public void setGyldigTil(LocalDate gyldigTil) {
        this.gyldigTil = gyldigTil;
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

    public String getAktørId() {
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

    public boolean isAktiv() {
        return aktiv;
    }

    public void setAktiv(boolean aktiv) {
        this.aktiv = aktiv;
    }

    public SøknadType getHandling() {
        return handling;
    }

    public void setHandling(SøknadType handling) {
        this.handling = handling;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[id=" + id + ", aktørId=" + aktørId + ", melding=" + melding + ", saknr="
                + saknr + ", dato=" + dato + ", kanal=" + kanal + ", gyldigTil=" + gyldigTil + ", handling=" + handling
                + ", aktiv=" + aktiv + "]";
    }

}
