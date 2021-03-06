package no.nav.foreldrepenger.historikk.tjenester.tilbakekreving;

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
import no.nav.foreldrepenger.historikk.domain.Fødselsnummer;
import no.nav.foreldrepenger.historikk.tjenester.felles.HendelseType;
import no.nav.foreldrepenger.historikk.tjenester.felles.YtelseType;

@Entity
@Table(name = "tilbakekreving")
@EntityListeners(AuditingEntityListener.class)
public class JPATilbakekrevingInnslag {

    private String journalpostId;
    @Enumerated(STRING)
    private YtelseType ytelseType;
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private int id;
    @Embedded
    private AktørId aktørId;
    @Embedded
    private Fødselsnummer fnr;

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

    JPATilbakekrevingInnslag() {
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

    public String getJournalpostId() {
        return journalpostId;
    }

    public void setJournalpostId(String journalpostId) {
        this.journalpostId = journalpostId;
    }

    public YtelseType getYtelseType() {
        return ytelseType;
    }

    public void setYtelseType(YtelseType ytelseType) {
        this.ytelseType = ytelseType;
    }

    public Fødselsnummer getFnr() {
        return fnr;
    }

    public void setFnr(Fødselsnummer fnr) {
        this.fnr = fnr;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[journalpostId=" + journalpostId + ", ytelseType=" + ytelseType + ", id="
                + id + ", fnr=" + fnr + ", aktørId=" + aktørId + ", tekst=" + tekst + ", hendelse=" + hendelse
                + ", gyldigTil=" + gyldigTil + ", saksnr=" + saksnr + ", opprettet=" + opprettet + ", endret=" + endret
                + ", aktiv=" + aktiv + ", innsendt=" + innsendt + ", dialogId=" + dialogId + "]";
    }

}
