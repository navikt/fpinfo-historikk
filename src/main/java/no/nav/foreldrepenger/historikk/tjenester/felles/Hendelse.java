package no.nav.foreldrepenger.historikk.tjenester.felles;

import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;
import no.nav.foreldrepenger.historikk.domain.AktørId;

public abstract class Hendelse {

    private final AktørId aktørId;
    @ApiModelProperty(example = "1234567890")
    private final String journalId;
    @ApiModelProperty(example = "123")
    private final String dokumentId;
    @ApiModelProperty(example = "42")
    private final String saksnummer;
    @NotNull
    private final HendelseType hendelse;
    @ApiModelProperty(example = "2019-10-22T18:50:10.9851661")
    private final LocalDateTime innsendt;

    public Hendelse(AktørId aktørId, String journalId, String dokumentId,
            String saksnummer, HendelseType hendelse, LocalDateTime innsendt) {
        this.aktørId = aktørId;
        this.journalId = journalId;
        this.dokumentId = dokumentId;
        this.saksnummer = saksnummer;
        this.hendelse = hendelse;
        this.innsendt = innsendt;
    }

    public String getDokumentId() {
        return dokumentId;
    }

    public LocalDateTime getInnsendt() {
        return innsendt;
    }

    public HendelseType getHendelse() {
        return hendelse;
    }

    public AktørId getAktørId() {
        return aktørId;
    }

    @JsonAlias("jounalpostId")
    public String getJournalId() {
        return journalId;
    }

    @JsonAlias("saksnr")
    public String getSaksnummer() {
        return saksnummer;
    }

    @JsonIgnore
    public boolean erEttersending() {
        return getHendelse().erEttersending();
    }

}
