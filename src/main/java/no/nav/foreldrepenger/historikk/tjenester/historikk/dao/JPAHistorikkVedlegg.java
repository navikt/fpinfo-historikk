package no.nav.foreldrepenger.historikk.tjenester.historikk.dao;

import static javax.persistence.FetchType.LAZY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "historikkvedlegg")
public class JPAHistorikkVedlegg {
    @Id
    @GeneratedValue
    private int id;
    @Column(name = "vedlegg_id")
    private String vedlegg;
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "vedlegg")
    private JPAHistorikkInnslag innslag;

    public JPAHistorikkVedlegg() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVedleggId() {
        return vedlegg;
    }

    public void setVedleggId(String vedleggId) {
        this.vedlegg = vedleggId;
    }

    public JPAHistorikkInnslag getInnslag() {
        return innslag;
    }

    public void setInnslag(JPAHistorikkInnslag innslag) {
        this.innslag = innslag;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[id=" + id + ", vedleggId=" + vedlegg + "]";
    }

}
