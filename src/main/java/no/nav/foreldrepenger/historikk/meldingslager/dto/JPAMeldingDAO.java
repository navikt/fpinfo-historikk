package no.nav.foreldrepenger.historikk.meldingslager.dto;

import static javax.persistence.GenerationType.IDENTITY;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "melding")
public class JPAMeldingDAO {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private int id;
    private String aktørId;
    private String melding;
    private LocalDate dato;

    public LocalDate getDato() {
        return dato;
    }

    public void setDato(LocalDate dato) {
        this.dato = dato;
    }

    private JPAMeldingDAO() {
    }

    public JPAMeldingDAO(String aktørId, String melding) {
        this.aktørId = aktørId;
        this.melding = melding;
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

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [id=" + id + ", aktørId=" + aktørId + ", melding=" + melding + ", dato="
                + dato + "]";
    }

}
