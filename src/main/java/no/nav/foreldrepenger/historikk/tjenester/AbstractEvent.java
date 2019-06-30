package no.nav.foreldrepenger.historikk.tjenester;

import no.nav.foreldrepenger.historikk.domain.AktørId;
import no.nav.foreldrepenger.historikk.domain.Fødselsnummer;
import no.nav.foreldrepenger.historikk.tjenester.innsending.Hendelse;

public class AbstractEvent {

    private final AktørId aktørId;
    private final Fødselsnummer fnr;
    private final String journalId;
    private final String referanseId;
    private final String saksNr;
    private final Hendelse hendelse;

    public AbstractEvent(AktørId aktørId, Fødselsnummer fnr, String journalId, String referanseId, String saksNr,
            Hendelse hendelse) {
        this.aktørId = aktørId;
        this.fnr = fnr;
        this.journalId = journalId;
        this.referanseId = referanseId;
        this.saksNr = saksNr;
        this.hendelse = hendelse;
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

    public Hendelse getHendelse() {
        return hendelse;
    }

}
