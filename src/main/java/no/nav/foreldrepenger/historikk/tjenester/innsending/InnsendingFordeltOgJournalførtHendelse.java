package no.nav.foreldrepenger.historikk.tjenester.innsending;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import no.nav.foreldrepenger.historikk.domain.Fødselsnummer;

public class InnsendingFordeltOgJournalførtHendelse {
    private final String journalpostId;
    private final String forsendelseId;
    private final Fødselsnummer fnr;
    private final String saksnr;

    @JsonCreator
    public InnsendingFordeltOgJournalførtHendelse(
            @JsonProperty("journalpostId") String journalpostId,
            @JsonProperty("journalpostId") String forsendelseId,
            @JsonProperty("fnr") Fødselsnummer fnr,
            @JsonProperty("saksnummer") String saksnummer) {
        this.journalpostId = journalpostId;
        this.forsendelseId = forsendelseId;
        this.saksnr = saksnummer;
        this.fnr = fnr;
    }

    public String getJournalpostId() {
        return journalpostId;
    }

    public String getForsendelseId() {
        return forsendelseId;
    }

    public Fødselsnummer getFnr() {
        return fnr;
    }

    public String getSaksnr() {
        return saksnr;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[journalpostId=" + journalpostId + ", forsendelseId=" + forsendelseId
                + ", fnr=" + fnr + ", saksnr=" + saksnr + "]";
    }

}
