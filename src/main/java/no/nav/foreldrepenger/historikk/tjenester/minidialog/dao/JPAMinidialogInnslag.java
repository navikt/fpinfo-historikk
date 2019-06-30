package no.nav.foreldrepenger.historikk.tjenester.minidialog.dao;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.IDENTITY;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import no.nav.foreldrepenger.historikk.tjenester.innsending.SøknadType;

@Entity
@Table(name = "minidialog")
@EntityListeners(AuditingEntityListener.class)
public class JPAMinidialogInnslag {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private int id;
    private String aktørId;
    private String fnr;
    private boolean janei;
    private String vedlegg;
    private String tekst;
    private LocalDate gyldigTil;
    @Enumerated(STRING)
    private SøknadType handling;
    private String saksnr;
    @CreatedDate
    private LocalDateTime opprettet;
    @LastModifiedDate
    private LocalDateTime endret;
    private boolean aktiv;

    public JPAMinidialogInnslag() {
    }

    public String getFnr() {
        return fnr;
    }

    public void setFnr(String fnr) {
        this.fnr = fnr;
    }

    public boolean isJanei() {
        return janei;
    }

    public void setJanei(boolean janei) {
        this.janei = janei;
    }

    public LocalDateTime getOpprettet() {
        return opprettet;
    }

    public void setOpprettet(LocalDateTime opprettet) {
        this.opprettet = opprettet;
    }

    public boolean isAktiv() {
        return aktiv;
    }

    public void setAktiv(boolean aktiv) {
        this.aktiv = aktiv;
    }

    public LocalDate getGyldigTil() {
        return gyldigTil;
    }

    public void setGyldigTil(LocalDate gyldigTil) {
        this.gyldigTil = gyldigTil;
    }

    public LocalDateTime getEndret() {
        return endret;
    }

    public void setEndret(LocalDateTime endret) {
        this.endret = endret;
    }

    public String getAktørId() {
        return aktørId;
    }

    public String getTekst() {
        return tekst;
    }

    public int getId() {
        return id;
    }

    public String getSaksnr() {
        return saksnr;
    }

    public SøknadType getHandling() {
        return handling;
    }

    public void setHandling(SøknadType handling) {
        this.handling = handling;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setAktørId(String aktørId) {
        this.aktørId = aktørId;
    }

    public void setTekst(String tekst) {
        this.tekst = tekst;
    }

    public void setSaksnr(String saksnr) {
        this.saksnr = saksnr;
    }

    public String getVedlegg() {
        return vedlegg;
    }

    public void setVedlegg(String vedlegg) {
        this.vedlegg = vedlegg;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[id=" + id + ", aktørId=" + aktørId + ", fnr=" + fnr + ", janei=" + janei
                + ", vedlegg=" + vedlegg + ", tekst=" + tekst + ", gyldigTil=" + gyldigTil + ", handling=" + handling
                + ", saksnr=" + saksnr + ", opprettet=" + opprettet + ", endret=" + endret + ", aktiv=" + aktiv + "]";
    }

}
