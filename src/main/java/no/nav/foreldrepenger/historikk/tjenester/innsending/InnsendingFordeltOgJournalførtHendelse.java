package no.nav.foreldrepenger.historikk.tjenester.innsending;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import no.nav.foreldrepenger.historikk.domain.Fødselsnummer;
import no.nav.foreldrepenger.historikk.tjenester.felles.Hendelse;
import no.nav.foreldrepenger.historikk.tjenester.felles.HendelseType;

public class InnsendingFordeltOgJournalførtHendelse extends Hendelse {
    private final String forsendelseId;
    private final Fødselsnummer fnr;
    private final String saksnr;

    @JsonCreator
    public InnsendingFordeltOgJournalførtHendelse(
            @JsonProperty("journalpostId") String journalpostId,
            @JsonProperty("forsendelseId") String forsendelseId,
            @JsonProperty("fnr") Fødselsnummer fnr,
            @JsonProperty("saksnummer") String saksnummer) {
        super(null, fnr, journalpostId, null, saksnummer, HendelseType.FORDELING, LocalDateTime.now());
        this.forsendelseId = forsendelseId;
        this.saksnr = saksnummer;
        this.fnr = fnr;
    }

    public String getForsendelseId() {
        return forsendelseId;
    }

    @Override
    public Fødselsnummer getFnr() {
        return fnr;
    }

    public String getSaksnr() {
        return saksnr;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[journalpostId=" + getJournalpostId() + ", forsendelseId=" + forsendelseId
                + ", fnr=" + fnr + ", saksnr=" + saksnr + "]";
    }

}
