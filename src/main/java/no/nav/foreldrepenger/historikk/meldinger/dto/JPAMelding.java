package no.nav.foreldrepenger.historikk.meldinger.dto;

import static javax.persistence.GenerationType.IDENTITY;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "melding")
@EntityListeners(JPAMeldingListener.class)
public class JPAMelding {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private int id;
    private String aktørId;
    private String melding;
    @Column(insertable = false, updatable = false)
    private LocalDate dato;
    private String saksnr;
    private String kanal;
    private LocalDate lest;

    public void setLest(LocalDate lest) {
        this.lest = lest;
    }

    public LocalDate getLest() {
        return lest;
    }

    private JPAMelding() {
    }

    public JPAMelding(String aktørId, String melding, String saksnr, String kanal) {
        this.aktørId = aktørId;
        this.melding = melding;
        this.saksnr = saksnr;
        this.kanal = kanal;
    }

    public String getKanal() {
        return kanal;
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
                + dato + ", saksnr=" + saksnr + ", kanal=" + kanal + "]";
    }

}
