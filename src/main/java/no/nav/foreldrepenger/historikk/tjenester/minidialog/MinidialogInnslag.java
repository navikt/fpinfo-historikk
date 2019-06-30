package no.nav.foreldrepenger.historikk.tjenester.minidialog;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import no.nav.foreldrepenger.historikk.tjenester.innsending.SøknadType;

public class MinidialogInnslag {

    private long id;
    private final String aktørId;
    private String fnr;
    private boolean janei;
    private String vedlegg;
    private String navn;
    private final String tekst;
    private final String saksnr;
    private LocalDateTime opprettet;
    private LocalDateTime endret;
    @DateTimeFormat(iso = ISO.DATE)
    private LocalDate gyldigTil;
    private SøknadType handling;
    private boolean aktiv;

    @JsonCreator
    public MinidialogInnslag(@JsonProperty("aktørId") String aktørId, @JsonProperty("tekst") String tekst,
            @JsonProperty("saksnr") String saksnr) {
        this.aktørId = aktørId;
        this.tekst = tekst;
        this.saksnr = saksnr;
    }

    public String getNavn() {
        return navn;
    }

    public void setNavn(String navn) {
        this.navn = navn;
    }

    public void setFnr(String fnr) {
        this.fnr = fnr;
    }

    public String getFnr() {
        return fnr;
    }

    public LocalDate getGyldigTil() {
        return gyldigTil;
    }

    public void setGyldigTil(LocalDate gyldigTil) {
        this.gyldigTil = gyldigTil;
    }

    public String getAktørId() {
        return aktørId;
    }

    public String getTekst() {
        return tekst;
    }

    public String getSaksnr() {
        return saksnr;
    }

    public boolean isJanei() {
        return janei;
    }

    public void setJanei(boolean janei) {
        this.janei = janei;
    }

    public String getVedlegg() {
        return vedlegg;
    }

    public void setVedlegg(String vedlegg) {
        this.vedlegg = vedlegg;
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

    public LocalDateTime getOpprettet() {
        return opprettet;
    }

    public void setOpprettet(LocalDateTime opprettet) {
        this.opprettet = opprettet;
    }

    public LocalDateTime getEndret() {
        return endret;
    }

    public void setEndret(LocalDateTime endret) {
        this.endret = endret;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[id=" + id + ", aktørId=" + aktørId + ", fnr=" + fnr + ", janei=" + janei
                + ", vedlegg=" + vedlegg + ", navn=" + navn + ", tekst=" + tekst + ", saksnr=" + saksnr + ", opprettet="
                + opprettet + ", endret=" + endret + ", gyldigTil=" + gyldigTil + ", handling=" + handling + ", aktiv="
                + aktiv + "]";
    }

}
