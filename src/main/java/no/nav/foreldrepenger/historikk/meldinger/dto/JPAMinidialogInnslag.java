package no.nav.foreldrepenger.historikk.meldinger.dto;

import static javax.persistence.GenerationType.IDENTITY;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "melding")
public class JPAMinidialogInnslag {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private int id;
    private String aktørId;
    private String melding;
    @Column(insertable = false, updatable = false)
    private LocalDate dato;
    private LocalDate gyldigTil;
    private String saksnr;
    private String kanal;

    private JPAMinidialogInnslag() {
    }

    public JPAMinidialogInnslag(String aktørId, String melding, String saksnr, String kanal) {
        this.aktørId = aktørId;
        this.melding = melding;
        this.saksnr = saksnr;
        this.kanal = kanal;
    }

    public LocalDate getGyldigTil() {
        return gyldigTil;
    }

    public void setGyldigTil(LocalDate gyldigTil) {
        this.gyldigTil = gyldigTil;
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
        return getClass().getSimpleName() + "[id=" + id + ", aktørId=" + aktørId + ", melding=" + melding + ", dato="
                + dato + ", gyldigTil=" + gyldigTil + ", saksnr=" + saksnr + ", kanal=" + kanal + "]";
    }

}
