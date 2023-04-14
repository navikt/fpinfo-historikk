package no.nav.foreldrepenger.historikk.tjenester.inntektsmelding;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;
import static no.nav.foreldrepenger.common.util.StringUtil.limitLast;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import no.nav.foreldrepenger.historikk.domain.AktørId;
import no.nav.foreldrepenger.historikk.domain.Versjon;

@Entity
@Table(name = "inntektsmelding")
@EntityListeners(AuditingEntityListener.class)
public class JPAInntektsmeldingInnslag {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private int id;
    @Embedded
    private AktørId aktørId;
    private String journalpostId;
    private String saksnr;
    @CreatedDate
    private LocalDateTime opprettet;
    private LocalDateTime innsendt;
    @Column(name = "startdato")
    private LocalDate startDato;

    private String arbeidsgiver;
    private String referanseId;
    @Embedded
    private Versjon versjon;
    @Enumerated(STRING)
    private InntektsmeldingType type;

    JPAInntektsmeldingInnslag() {
    }

    public InntektsmeldingType getType() {
        return type;
    }

    public void setType(InntektsmeldingType type) {
        this.type = type;
    }

    public LocalDate getStartDato() {
        return startDato;
    }

    public void setStartDato(LocalDate startDato) {
        this.startDato = startDato;
    }

    public LocalDateTime getInnsendt() {
        return innsendt;
    }

    public void setInnsendt(LocalDateTime innsendt) {
        this.innsendt = innsendt;
    }

    public Versjon getVersjon() {
        return versjon;
    }

    public void setVersjon(Versjon versjon) {
        this.versjon = versjon;
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

    public String getArbeidsgiver() {
        return arbeidsgiver;
    }

    public void setArbeidsgiver(String arbeidsgiver) {
        this.arbeidsgiver = arbeidsgiver;
    }

    public String getReferanseId() {
        return referanseId;
    }

    public void setReferanseId(String referanseId) {
        this.referanseId = referanseId;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[id=" + id + ", aktørId=" + aktørId + ", journalpostId=" + journalpostId
                + ", saksnr=" + saksnr + ", opprettet=" + opprettet + ", innsendt=" + innsendt
                + ", startDato=" + startDato + ", arbeidsgiver=" + limitLast(arbeidsgiver, 3) + ", referanseId=" + referanseId
                + ", versjon=" + versjon + ", type=" + type + "]";
    }

}
