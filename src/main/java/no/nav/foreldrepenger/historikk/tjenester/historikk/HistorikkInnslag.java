package no.nav.foreldrepenger.historikk.tjenester.historikk;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import no.nav.foreldrepenger.historikk.domain.AktørId;
import no.nav.foreldrepenger.historikk.domain.Fødselsnummer;

class HistorikkInnslag {

    private final String tekst;
    private final Fødselsnummer fnr;
    private AktørId aktørId;
    private String journalpostId;
    private String saksnr;
    private LocalDateTime opprettet;

    @JsonCreator
    public HistorikkInnslag(@JsonProperty("fnr") Fødselsnummer fnr, @JsonProperty("tekst") String tekst) {
        this.fnr = fnr;
        this.tekst = tekst;
    }

    public void setAktørId(AktørId aktørId) {
        this.aktørId = aktørId;
    }

    public AktørId getAktørId() {
        return aktørId;
    }

    public Fødselsnummer getFnr() {
        return fnr;
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
