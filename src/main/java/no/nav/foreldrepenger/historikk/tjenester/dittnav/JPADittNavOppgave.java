package no.nav.foreldrepenger.historikk.tjenester.dittnav;

import static javax.persistence.GenerationType.IDENTITY;

import java.time.LocalDateTime;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

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
    private String saksnr;
    @CreatedDate
    private LocalDateTime opprettet;
    private String referanseId;

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

    public Fødselsnummer getFnr() {
        return fnr;
    }

    public void setFnr(Fødselsnummer fnr) {
        this.fnr = fnr;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [id=" + id + ", fnr=" + fnr + ", saksnr=" + saksnr + ", opprettet="
                + opprettet
                + ", referanseId=" + referanseId + "]";
    }

}
