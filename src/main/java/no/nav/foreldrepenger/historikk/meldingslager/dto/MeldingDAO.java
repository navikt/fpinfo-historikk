package no.nav.foreldrepenger.historikk.meldingslager.dto;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class MeldingDAO {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private int id;
    private String aktørId;
    private String melding;

    private MeldingDAO() {
    }

    public MeldingDAO(String aktørId, String melding) {
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
        return getClass().getSimpleName() + " [id=" + id + ", aktørId=" + aktørId + ", melding=" + melding + "]";
    }
}
