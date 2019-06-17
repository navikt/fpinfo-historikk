package no.nav.foreldrepenger.historikk.meldinger.dto;

import static javax.persistence.GenerationType.IDENTITY;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "historikk")
public class JPAHistorikkInnslag {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private int id;
    private String aktørId;
    private String journalpostId;
    private String tekst;
    @Column(insertable = false, updatable = false)
    private LocalDateTime datoMottatt;
    private String saksnr;

    private JPAHistorikkInnslag() {
    }

    public JPAHistorikkInnslag(String aktørId, String tekst) {
        this.aktørId = aktørId;
        this.tekst = tekst;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAktørId() {
        return aktørId;
    }

    public void setAktørId(String aktørId) {
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
        return getClass().getSimpleName() + "[id=" + id + ", aktørId=" + aktørId + ", journalpostId=" + journalpostId
                + ", tekst=" + tekst + ", datoMottatt=" + datoMottatt + ", saksnr="
                + saksnr + "]";
    }
}
