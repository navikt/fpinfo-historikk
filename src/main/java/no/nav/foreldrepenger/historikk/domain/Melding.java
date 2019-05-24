package no.nav.foreldrepenger.historikk.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Melding {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    public void setId(int id) {
        this.id = id;
    }

    private String fnr;
    private String melding;

    protected Melding() {
    }

    public Melding(String fnr, String melding) {
        this.fnr = fnr;
        this.melding = melding;
    }

    public String getFnr() {
        return fnr;
    }

    public String getMelding() {
        return melding;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [id=" + id + ", fnr=" + fnr + ", melding=" + melding + "]";
    }
}
