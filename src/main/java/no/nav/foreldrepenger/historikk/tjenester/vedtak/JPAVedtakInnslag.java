package no.nav.foreldrepenger.historikk.tjenester.vedtak;

import static javax.persistence.GenerationType.IDENTITY;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import no.nav.foreldrepenger.historikk.domain.Fødselsnummer;

@Entity
@Table(name = "vedtak")
@EntityListeners(AuditingEntityListener.class)
public class JPAVedtakInnslag {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private int id;

    private String vedtakReferanse;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVedtakReferanse() {
        return vedtakReferanse;
    }

    public void setVedtakReferanse(String vedtakReferanse) {
        this.vedtakReferanse = vedtakReferanse;
    }

    private LocalDate gyldigTil;
    private Fødselsnummer fnr;

    public Fødselsnummer getFnr() {
        return fnr;
    }

    public void setFnr(Fødselsnummer fnr) {
        this.fnr = fnr;
    }

    public LocalDate getGyldigTil() {
        return gyldigTil;
    }

    public void setGyldigTil(LocalDate gyldigTil) {
        this.gyldigTil = gyldigTil;
    }
}
