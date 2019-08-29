package no.nav.foreldrepenger.historikk.tjenester.historikk.dao;

import static javax.persistence.GenerationType.IDENTITY;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import no.nav.foreldrepenger.historikk.domain.AktørId;
import no.nav.foreldrepenger.historikk.domain.Fødselsnummer;

@Entity
@Table(name = "historikk")
@EntityListeners(AuditingEntityListener.class)
public class JPAHistorikkInnslag {
    public List<JPAHistorikkVedlegg> getVedlegg() {
        return vedlegg;
    }

    public void setVedlegg(List<JPAHistorikkVedlegg> vedlegg) {
        this.vedlegg = vedlegg;
    }

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private int id;
    @Embedded
    private AktørId aktørId;
    @Embedded
    private Fødselsnummer fnr;
    private String journalpostId;
    private String saksnr;
    private String tekst;
    @CreatedDate
    private LocalDateTime opprettet;
    @OneToMany(mappedBy = "innslag", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<JPAHistorikkVedlegg> vedlegg = new ArrayList<>();

    public JPAHistorikkInnslag() {
    }

    public void addVedlegg(JPAHistorikkVedlegg v) {
        vedlegg.add(v);
        v.setInnslag(this);
    }

    public Fødselsnummer getFnr() {
        return fnr;
    }

    public void setFnr(Fødselsnummer fnr) {
        this.fnr = fnr;
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

    public String getTekst() {
        return tekst;
    }

    public void setTekst(String tekst) {
        this.tekst = tekst;
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
        return getClass().getSimpleName() + "[id=" + id + ", aktørId=" + aktørId + ", fnr=" + fnr + ", journalpostId="
                + journalpostId + ", saksnr=" + saksnr + ", tekst=" + tekst + ", opprettet=" + opprettet + ", vedlegg="
                + vedlegg + "]";
    }

}
