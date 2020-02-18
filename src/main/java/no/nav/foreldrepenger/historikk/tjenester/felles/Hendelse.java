package no.nav.foreldrepenger.historikk.tjenester.felles;

import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;
import no.nav.foreldrepenger.historikk.domain.AktørId;

public abstract class Hendelse {

    private final AktørId aktørId;
    @ApiModelProperty(example = "1234567890")
    private final String journalpostId;
    @ApiModelProperty(example = "123")
    private final String dokumentId;
    @ApiModelProperty(example = "42")
    private final String saksnummer;
    @NotNull
    private final HendelseType hendelse;
    @ApiModelProperty(example = "2019-10-22T18:50:10.9851661")
    private final LocalDateTime opprettet;

    public Hendelse(AktørId aktørId, String jounalpostId, String dokumentId,
            String saksnummer, HendelseType hendelse, LocalDateTime opprettet) {
        this.aktørId = aktørId;
        this.journalpostId = jounalpostId;
        this.dokumentId = dokumentId;
        this.saksnummer = saksnummer;
        this.hendelse = hendelse;
        this.opprettet = opprettet;
    }

    public String getDokumentId() {
        return dokumentId;
    }

    public LocalDateTime getOpprettet() {
        return opprettet;
    }

    public HendelseType getHendelse() {
        return hendelse;
    }

    public AktørId getAktørId() {
        return aktørId;
    }

    public String getJournalpostId() {
        return journalpostId;
    }

    public String getSaksnummer() {
        return saksnummer;
    }

    @JsonIgnore
    public boolean erEttersending() {
        return getHendelse().erEttersending();
    }

}
