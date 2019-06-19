package no.nav.foreldrepenger.historikk.domain;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class HistorikkInnslag {

    private final AktørId aktørId;
    private String journalpostId;
    private final String tekst;
    private LocalDateTime datoMottatt;
    private String saksnr;

    @JsonCreator
    public HistorikkInnslag(@JsonProperty("aktørId") AktørId aktørId, @JsonProperty("tekst") String tekst) {
        this.aktørId = aktørId;
        this.tekst = tekst;
    }

    public AktørId getAktørId() {
        return aktørId;
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

    public LocalDateTime getDatoMottatt() {
        return datoMottatt;
    }

    public void setDatoMottatt(LocalDateTime datoMottatt) {
        this.datoMottatt = datoMottatt;
    }

    public String getSaksnr() {
        return saksnr;
    }

    public void setSaksnr(String saksnr) {
        this.saksnr = saksnr;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[aktørId=" + aktørId + ", journalpostId=" + journalpostId + ", tekst="
                + tekst + ", datoMottatt=" + datoMottatt + ", saksnr=" + saksnr + "]";
    }
}
