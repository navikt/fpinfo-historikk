package no.nav.foreldrepenger.historikk.tjenester.dittnav;

import static jakarta.persistence.GenerationType.IDENTITY;

import java.time.LocalDateTime;

import jakarta.persistence.*;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import no.nav.foreldrepenger.historikk.domain.Fødselsnummer;

@Entity
@Table(name = "dittnavoppgaver")
@EntityListeners(AuditingEntityListener.class)
public class JPADittNavOppgave {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private int id;
    @Embedded
    private Fødselsnummer fnr;
    @CreatedDate
    private LocalDateTime opprettet;
    @Enumerated(EnumType.STRING)
    private NotifikasjonType type;
    private String referanseId;
    private String internReferanseId;
    private String eksternReferanseId;
    private String grupperingsId;
    private Boolean sendtDone;

    JPADittNavOppgave() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getOpprettet() {
        return opprettet;
    }

    public void setOpprettet(LocalDateTime opprettet) {
        this.opprettet = opprettet;
    }

    public String getReferanseId() {
        return referanseId;
    }

    public void setReferanseId(String referanseId) {
        this.referanseId = referanseId;
    }

    public String getEksternReferanseId() {
        return eksternReferanseId;
    }

    public void setEksternReferanseId(String eksternReferanseId) {
        this.eksternReferanseId = eksternReferanseId;
    }

    public String getInternReferanseId() {
        return internReferanseId;
    }

    public void setInternReferanseId(String referanseId) {
        this.internReferanseId = referanseId;
    }

    public String getGrupperingsId() {
        return grupperingsId;
    }

    public void setGrupperingsId(String grupperingsId) {
        this.grupperingsId = grupperingsId;
    }

    public Fødselsnummer getFnr() {
        return fnr;
    }

    public void setFnr(Fødselsnummer fnr) {
        this.fnr = fnr;
    }

    public NotifikasjonType getType() {
        return type;
    }

    public void setType(NotifikasjonType type) {
        this.type = type;
    }


    public boolean getSendtDoneMelding() {
        return sendtDone != null && sendtDone;
    }

    public void setSendtDoneMelding(boolean sendtDone) {
        this.sendtDone = sendtDone;
    }

    @PrePersist
    void defaultDone() {
        if (this.sendtDone == null) {
            this.sendtDone = false;
        }
    }

    @Override
    public String toString() {
        return "JPADittNavOppgave{" +
            "id=" + id +
            ", fnr=" + fnr +
            ", opprettet=" + opprettet +
            ", type=" + type +
            ", referanseId='" + referanseId + '\'' +
            ", internReferanseId='" + internReferanseId + '\'' +
            ", eksternReferanseId='" + eksternReferanseId + '\'' +
            ", grupperingsId='" + grupperingsId + '\'' +
            ", sendtDone=" + sendtDone +
            '}';
    }

    enum NotifikasjonType {
        OPPGAVE, BESKJED;
    }

}
