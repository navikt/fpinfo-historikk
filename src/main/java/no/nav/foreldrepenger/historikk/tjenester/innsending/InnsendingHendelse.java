package no.nav.foreldrepenger.historikk.tjenester.innsending;

import javax.validation.constraints.NotNull;

import no.nav.foreldrepenger.historikk.domain.AktørId;
import no.nav.foreldrepenger.historikk.domain.Fødselsnummer;

public abstract class InnsendingHendelse {

    private final AktørId aktørId;
    @NotNull
    private final Fødselsnummer fnr;
    private final String journalId;
    private final String referanseId;
    private final String saksNr;

    public InnsendingHendelse(AktørId aktørId, Fødselsnummer fnr, String journalId, String referanseId, String saksNr) {
        this.aktørId = aktørId;
        this.fnr = fnr;
        this.journalId = journalId;
        this.referanseId = referanseId;
        this.saksNr = saksNr;
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

}
