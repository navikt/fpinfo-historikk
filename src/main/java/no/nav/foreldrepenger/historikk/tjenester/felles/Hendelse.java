package no.nav.foreldrepenger.historikk.tjenester.felles;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;
import no.nav.foreldrepenger.historikk.domain.AktørId;

public abstract class Hendelse {

    @ApiModelProperty(example = "1111111111111")
    private final AktørId aktørId;
    private final String journalId;
    private final String saksnummer;
    @NotNull
    private final HendelseType hendelse;

    public Hendelse(AktørId aktørId, String journalId, String saksnummer,
            HendelseType hendelseType) {
        this.aktørId = aktørId;
        this.journalId = journalId;
        this.saksnummer = saksnummer;
        this.hendelse = hendelseType;
    }

    public HendelseType getHendelse() {
        return hendelse;
    }

    public AktørId getAktørId() {
        return aktørId;
    }

    public String getJournalId() {
        return journalId;
    }

    public String getSaksnummer() {
        return saksnummer;
    }

    @JsonIgnore
    public boolean erEttersending() {
        return getHendelse().erEttersending();
    }

}
