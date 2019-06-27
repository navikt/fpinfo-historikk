package no.nav.foreldrepenger.historikk.tjenester.minidialog.dao;

import static javax.persistence.GenerationType.IDENTITY;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "melding")
@EntityListeners(AuditingEntityListener.class)
public class JPAMinidialogInnslag {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private int id;
    private String aktørId;
    private String melding;
    private LocalDate gyldigTil;
    private String saksnr;
    private String handling;
    @CreatedDate
    private LocalDateTime opprettet;
    @LastModifiedDate
    private LocalDateTime endret;
    private boolean aktiv;

    private JPAMinidialogInnslag() {
    }

    public JPAMinidialogInnslag(String aktørId, String melding, String saksnr) {
        this.aktørId = aktørId;
        this.melding = melding;
        this.saksnr = saksnr;
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

    public String getMelding() {
        return melding;
    }

    public int getId() {
        return id;
    }

    public String getSaksnr() {
        return saksnr;
    }

    public String getHandling() {
        return handling;
    }

    public void setHandling(String handling) {
        this.handling = handling;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setAktørId(String aktørId) {
        this.aktørId = aktørId;
    }

    public void setMelding(String melding) {
        this.melding = melding;
    }

    public void setSaksnr(String saksnr) {
        this.saksnr = saksnr;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[id=" + id + ", aktørId=" + aktørId + ", melding=" + melding
                + ", gyldigTil=" + gyldigTil + ", saksnr=" + saksnr + ", handling=" + handling
                + ", opprettet=" + opprettet + ", endret=" + endret + ", aktiv=" + aktiv + "]";
    }

}
