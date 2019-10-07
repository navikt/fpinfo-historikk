package no.nav.foreldrepenger.historikk.tjenester.felles;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import no.nav.foreldrepenger.historikk.domain.AktørId;
import no.nav.foreldrepenger.historikk.domain.Fødselsnummer;

public abstract class Hendelse {

    private final AktørId aktørId;
    @NotNull
    private final Fødselsnummer fnr;
    private final String journalId;
    private final String referanseId;
    private final String saksNr;
    @NotNull
    private final HendelseType hendelseType;

    public Hendelse(AktørId aktørId, Fødselsnummer fnr, String journalId, String referanseId, String saksNr,
            HendelseType hendelseType) {
        this.aktørId = aktørId;
        this.fnr = fnr;
        this.journalId = journalId;
        this.referanseId = referanseId;
        this.saksNr = saksNr;
        this.hendelseType = hendelseType;
    }

    public HendelseType getHendelseType() {
        return hendelseType;
    }

    public AktørId getAktørId() {
        return aktørId;
    }

    public Fødselsnummer getFnr() {
        return fnr;
    }

    public String getJournalId() {
        return journalId;
    }

    public String getReferanseId() {
        return referanseId;
    }

    public String getSaksNr() {
        return saksNr;
    }

    @JsonIgnore
    public boolean erEttersending() {
        return getHendelseType().erEttersending();
    }

}
