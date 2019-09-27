package no.nav.foreldrepenger.historikk.tjenester.minidialog;

import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonCreator;

import io.swagger.annotations.ApiModelProperty;
import no.nav.foreldrepenger.historikk.domain.AktørId;
import no.nav.foreldrepenger.historikk.domain.Fødselsnummer;
import no.nav.foreldrepenger.historikk.tjenester.felles.Hendelse;
import no.nav.foreldrepenger.historikk.tjenester.felles.HendelseType;

public class MinidialogHendelse extends Hendelse {

    @ApiModelProperty(hidden = true)
    private long id;
    @ApiModelProperty(hidden = true)
    private LocalDateTime opprettet;
    @ApiModelProperty(hidden = true)
    private LocalDateTime endret;
    @ApiModelProperty(example = "2999-12-12")
    @DateTimeFormat(iso = DATE)
    private LocalDate gyldigTil;
    private boolean aktiv;

    private final String tekst;
    @ApiModelProperty(example = "Navn Navnesen")
    private final String navn;

    @JsonCreator
    public MinidialogHendelse(AktørId aktørId, Fødselsnummer fnr, String journalId, String referanseId, String saksNr,
            HendelseType hendelse, String tekst, String navn) {
        super(aktørId, fnr, journalId, referanseId, saksNr, hendelse);
        this.tekst = tekst;
        this.navn = navn;
    }

    public String getNavn() {
        return navn;
    }

    public String getTekst() {
        return tekst;
    }

    public LocalDate getGyldigTil() {
        return gyldigTil;
    }

    public void setGyldigTil(LocalDate gyldigTil) {
        this.gyldigTil = gyldigTil;
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

    public void setOpprettet(LocalDateTime opprettet) {
        this.opprettet = opprettet;
    }

    public LocalDateTime getEndret() {
        return endret;
    }

    public void setEndret(LocalDateTime endret) {
        this.endret = endret;
    }

}
