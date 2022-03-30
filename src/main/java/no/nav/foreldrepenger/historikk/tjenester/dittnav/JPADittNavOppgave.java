package no.nav.foreldrepenger.historikk.tjenester.dittnav;

import static javax.persistence.GenerationType.IDENTITY;

import java.time.LocalDateTime;

import javax.persistence.*;

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

    public void setEksternReferanseId(String eksternReferanseId) {
        this.eksternReferanseId = eksternReferanseId;
    }

    public void setInternReferanseId(String referanseId) {
        this.internReferanseId = referanseId;
    }

    public void setFnr(Fødselsnummer fnr) {
        this.fnr = fnr;
    }

    public void setType(NotifikasjonType type) {
        this.type = type;
    }

    public Fødselsnummer getFnr() {
        return fnr;
    }

    public NotifikasjonType getType() {
        return type;
    }

    public String getInternReferanseId() {
        return internReferanseId;
    }

    public String getEksternReferanseId() {
        return eksternReferanseId;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() +
            " [id=" + id +
            ", fnr=" + fnr +
            ", opprettet=" + opprettet +
            ", type=" + type +
            ", referanseId='" + referanseId + '\'' +
            ", internReferanseId='" + internReferanseId + '\'' +
            ", eksternReferanseId='" + eksternReferanseId + '\'' +
            '}';
    }

    enum NotifikasjonType {
        OPPGAVE, BESKJED;
    }

}
