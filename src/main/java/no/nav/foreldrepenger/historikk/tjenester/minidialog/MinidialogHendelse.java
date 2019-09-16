package no.nav.foreldrepenger.historikk.tjenester.minidialog;

import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;
import no.nav.foreldrepenger.historikk.domain.AktørId;
import no.nav.foreldrepenger.historikk.domain.Fødselsnummer;

public class MinidialogHendelse {

    @NotNull
    private final Fødselsnummer fnr;
    @NotNull
    @ApiModelProperty(example = "TILBAKEKREVING")
    private final String tekst;
    @ApiModelProperty(example = "Navn Navnesen")
    private String navn;
    @ApiModelProperty(hidden = true)
    private long id;
    private AktørId aktørId;
    @ApiModelProperty(example = "42")
    private String saksnr;
    @ApiModelProperty(hidden = true)
    private LocalDateTime opprettet;
    @ApiModelProperty(hidden = true)
    private LocalDateTime endret;
    @ApiModelProperty(example = "2999-12-12")
    @DateTimeFormat(iso = DATE)
    private LocalDate gyldigTil;
    private boolean aktiv;
    @ApiModelProperty(example = "1234567890")
    private String referanseId;

    @JsonCreator
    public MinidialogHendelse(@JsonProperty("fnr") Fødselsnummer fnr, @JsonProperty("tekst") String tekst) {
        this.fnr = fnr;
        this.tekst = tekst;
    }

    public String getNavn() {
        return navn;
    }

    public void setNavn(String navn) {
        this.navn = navn;
    }

    public Fødselsnummer getFnr() {
        return fnr;
    }

    public void setAktørId(AktørId aktørId) {
        this.aktørId = aktørId;
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

    public LocalDateTime getOpprettet() {
        return opprettet;
    }

    public void setSaksnr(String saksnr) {
        this.saksnr = saksnr;
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

    public String getReferanseId() {
        return referanseId;
    }

    public void setReferanseId(String referanseId) {
        this.referanseId = referanseId;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[fnr=" + fnr + ", tekst=" + tekst + ", navn=" + navn + ", id=" + id
                + ", aktørId=" + aktørId + ", saksnr=" + saksnr + ", opprettet=" + opprettet + ", endret=" + endret
                + ", gyldigTil=" + gyldigTil + ", aktiv=" + aktiv + ", referanseId=" + referanseId + "]";
    }

}
