package no.nav.foreldrepenger.historikk.tjenester.innsending;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.IDENTITY;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import no.nav.foreldrepenger.historikk.domain.AktørId;
import no.nav.foreldrepenger.historikk.tjenester.felles.HendelseType;

@Entity
@Table(name = "innsending")
@EntityListeners(AuditingEntityListener.class)
public class JPAInnsendingInnslag {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private int id;
    @Embedded
    private AktørId aktørId;
    private String journalpostId;
    private String saksnr;
    @Enumerated(STRING)
    private HendelseType hendelse;
    @CreatedDate
    private LocalDateTime opprettet;
    @OneToMany(mappedBy = "innslag", cascade = ALL, orphanRemoval = true)
    private List<JPAInnsendingVedlegg> vedlegg = new ArrayList<>();
    private LocalDate behandlingsdato;
    private String referanseId;

    JPAInnsendingInnslag() {
    }

    public LocalDate getBehandlingsdato() {
        return behandlingsdato;
    }

    public void setBehandlingsdato(LocalDate behandlingsdato) {
        this.behandlingsdato = behandlingsdato;
    }

    public void addVedlegg(JPAInnsendingVedlegg v) {
        vedlegg.add(v);
        v.setInnslag(this);
    }

    public List<JPAInnsendingVedlegg> getVedlegg() {
        return vedlegg;
    }

    public void setVedlegg(List<JPAInnsendingVedlegg> vedlegg) {
        this.vedlegg = vedlegg;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public HendelseType getHendelse() {
        return hendelse;
    }

    public void setHendelse(HendelseType hendelse) {
        this.hendelse = hendelse;
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

    public String getReferanseId() {
        return referanseId;
    }

    public void setReferanseId(String referanseId) {
        this.referanseId = referanseId;
    }

    public void setSaksnr(String saksnr) {
        this.saksnr = saksnr;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[id=" + id + ", aktørId=" + aktørId + ", journalpostId="
                + journalpostId + ", saksnr=" + saksnr + ", hendelse=" + hendelse + ", opprettet=" + opprettet
                + ", vedlegg=" + vedlegg + ", behandlingsdato=" + behandlingsdato + ", referanseId=" + referanseId
                + "]";
    }

}
