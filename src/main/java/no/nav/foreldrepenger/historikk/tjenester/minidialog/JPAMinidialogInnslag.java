package no.nav.foreldrepenger.historikk.tjenester.minidialog;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.IDENTITY;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import no.nav.foreldrepenger.historikk.domain.AktørId;
import no.nav.foreldrepenger.historikk.tjenester.felles.HendelseType;

@Entity
@Table(name = "minidialog")
@EntityListeners(AuditingEntityListener.class)
public class JPAMinidialogInnslag {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private int id;
    @Embedded
    private AktørId aktørId;
    private String tekst;
    @Enumerated(STRING)
    private HendelseType hendelse;

    private LocalDate gyldigTil;

    private String saksnr;
    @CreatedDate
    private LocalDateTime opprettet;
    @LastModifiedDate
    private LocalDateTime endret;
    private boolean aktiv;
    private LocalDateTime innsendt;

    private String dialogId;

    JPAMinidialogInnslag() {
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

    public AktørId getAktørId() {
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

    public void setId(int id) {
        this.id = id;
    }

    public void setAktørId(AktørId aktørId) {
        this.aktørId = aktørId;
    }

    public void setTekst(String tekst) {
        this.tekst = tekst;
    }

    public void setSaksnr(String saksnr) {
        this.saksnr = saksnr;
    }

    public HendelseType getHendelse() {
        return hendelse;
    }

    public void setHendelse(HendelseType hendelse) {
        this.hendelse = hendelse;
    }

    public String getDialogId() {
        return dialogId;
    }

    public void setDialogId(String dialogId) {
        this.dialogId = dialogId;
    }

    public LocalDateTime getInnsendt() {
        return innsendt;
    }

    public void setInnsendt(LocalDateTime innsendt) {
        this.innsendt = innsendt;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[id=" + id + ", aktørId=" + aktørId + ", tekst=" + tekst + ", hendelse="
                + hendelse + ", gyldigTil=" + gyldigTil + ", saksnr=" + saksnr + ", opprettet=" + opprettet
                + ", endret=" + endret + ", aktiv=" + aktiv + ", innsendt=" + innsendt + ", dialogId=" + dialogId + "]";
    }

}
