package no.nav.foreldrepenger.historikk.tjenester.minidialog;

import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import no.nav.foreldrepenger.historikk.domain.AktørId;
import no.nav.foreldrepenger.historikk.domain.Fødselsnummer;
import no.nav.foreldrepenger.historikk.tjenester.innsending.Hendelse;

public class MinidialogInnslag {

    private long id;
    private final AktørId aktørId;
    private Fødselsnummer fnr;
    private boolean janei;
    private String vedlegg;
    private String navn;
    private final String tekst;
    private final String saksnr;
    private LocalDateTime opprettet;
    private LocalDateTime endret;
    @DateTimeFormat(iso = DATE)
    private LocalDate gyldigTil;
    private Hendelse handling;
    private boolean aktiv;
    private String referanseId;

    public String getReferanseId() {
        return referanseId;
    }

    public void setReferanseId(String referanseId) {
        this.referanseId = referanseId;
    }

    @JsonCreator
    public MinidialogInnslag(@JsonProperty("aktørId") AktørId aktørId, @JsonProperty("tekst") String tekst,
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

    public void setFnr(Fødselsnummer fnr) {
        this.fnr = fnr;
    }

    public Fødselsnummer getFnr() {
        return fnr;
    }

    public LocalDate getGyldigTil() {
        return gyldigTil;
    }

    public void setGyldigTil(LocalDate gyldigTil) {
        this.gyldigTil = gyldigTil;
    }

    public AktørId getAktørId() {
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

    public Hendelse getHandling() {
        return handling;
    }

    public void setHandling(Hendelse handling) {
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
