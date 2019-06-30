package no.nav.foreldrepenger.historikk.tjenester.historikk;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import no.nav.foreldrepenger.historikk.domain.AktørId;
import no.nav.foreldrepenger.historikk.domain.Fødselsnummer;

public class HistorikkInnslag {

    private final AktørId aktørId;
    private Fødselsnummer fnr;

    private String journalpostId;
    private String saksnr;
    private final String tekst;
    private LocalDateTime opprettet;

    @JsonCreator
    public HistorikkInnslag(@JsonProperty("aktørId") AktørId aktørId, @JsonProperty("tekst") String tekst) {
        this.aktørId = aktørId;
        this.tekst = tekst;
    }

    public AktørId getAktørId() {
        return aktørId;
    }

    public Fødselsnummer getFnr() {
        return fnr;
    }

    public void setFnr(Fødselsnummer fnr) {
        this.fnr = fnr;
    }

    public String getJournalpostId() {
        return journalpostId;
    }

    public void setJournalpostId(String journalpostId) {
        this.journalpostId = journalpostId;
    }

    public String getTekst() {
        return tekst;
    }

    public LocalDateTime getOpprettet() {
        return opprettet;
    }

    public void setOpprettet(LocalDateTime opprettet) {
        this.opprettet = opprettet;
    }

    public String getSaksnr() {
        return saksnr;
    }

    public void setSaksnr(String saksnr) {
        this.saksnr = saksnr;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[aktørId=" + aktørId + ", fnr=" + fnr + ", journalpostId="
                + journalpostId + ", saksnr=" + saksnr + ", tekst=" + tekst + ", opprettet=" + opprettet + "]";
    }

}
