package no.nav.foreldrepenger.historikk.tjenester.felles;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.v3.oas.annotations.media.Schema;
import no.nav.foreldrepenger.historikk.domain.AktørId;
import no.nav.foreldrepenger.historikk.domain.Fødselsnummer;

public abstract class Hendelse {

    private final AktørId aktørId;
    private final Fødselsnummer fnr;

    @Schema(example = "1234567890")
    private final String journalpostId;
    @Schema(example = "123")
    private final String dokumentId;
    @Schema(example = "42")
    private final String saksnummer;
    @NotNull
    private final HendelseType hendelse;
    @Schema(example = "2019-10-22T18:50:10.9851661")
    private final LocalDateTime opprettet;

    protected Hendelse(AktørId aktørId, Fødselsnummer fnr, String jounalpostId, String dokumentId,
            String saksnummer, HendelseType hendelse, LocalDateTime opprettet) {
        this.aktørId = aktørId;
        this.fnr = fnr;
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

    public Fødselsnummer getFnr() {
        return fnr;
    }

    public String getSaksnummer() {
        return saksnummer;
    }

    @JsonIgnore
    public boolean erEttersending() {
        return getHendelse().erEttersending();
    }

}
