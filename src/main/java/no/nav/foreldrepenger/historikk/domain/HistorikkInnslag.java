package no.nav.foreldrepenger.historikk.domain;

import java.time.LocalDateTime;

public class HistorikkInnslag {

    private AktørId aktørId;
    private String journalpostId;
    private String tekst;
    private LocalDateTime datoMottatt;
    private String saksnr;

    private HistorikkInnslag() {
    }

    public HistorikkInnslag(AktørId aktørId, String tekst) {
        this.aktørId = aktørId;
        this.tekst = tekst;
    }

    public AktørId getAktørId() {
        return aktørId;
    }

    public void setAktørId(AktørId aktørId) {
        this.aktørId = aktørId;
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

    public void setTekst(String tekst) {
        this.tekst = tekst;
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
