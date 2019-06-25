package no.nav.foreldrepenger.historikk.tjenester.journalføring;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class JournalføringRespons {
    private final String journalpostId;
    private final String melding;
    private final String journalstatus;

    @JsonCreator
    public JournalføringRespons(@JsonProperty("journalpostId") String journalpostId,
            @JsonProperty("melding") String melding, @JsonProperty("journalstatus") String journalstatus) {
        this.journalpostId = journalpostId;
        this.melding = melding;
        this.journalstatus = journalstatus;
    }

    public String getJournalpostId() {
        return journalpostId;
    }

    public String getMelding() {
        return melding;
    }

    public String getJournalstatus() {
        return journalstatus;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[journalpostId=" + journalpostId + ", melding=" + melding
                + ", journalstatus=" + journalstatus + "]";
    }

}
