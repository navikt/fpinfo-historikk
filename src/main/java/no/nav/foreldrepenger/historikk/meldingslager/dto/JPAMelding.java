package no.nav.foreldrepenger.historikk.meldingslager.dto;

import static javax.persistence.GenerationType.IDENTITY;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "melding")
public class JPAMelding {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private int id;
    private String aktørId;
    private String melding;
    @Column(insertable = false, updatable = false)
    private LocalDate dato;
    private String saksnr;

    private JPAMelding() {
    }

    public JPAMelding(String aktørId, String melding, String saksnr) {
        this.aktørId = aktørId;
        this.melding = melding;
        this.saksnr = saksnr;
    }

    public LocalDate getDato() {
        return dato;
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

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [id=" + id + ", aktørId=" + aktørId + ", melding=" + melding + ", dato="
                + dato + ", saksnr=" + saksnr + "]";
    }

}
