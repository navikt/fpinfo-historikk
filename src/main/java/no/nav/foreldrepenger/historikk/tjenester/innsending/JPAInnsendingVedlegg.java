package no.nav.foreldrepenger.historikk.tjenester.innsending;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.LAZY;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "historikkvedlegg")
public class JPAInnsendingVedlegg {
    @Id
    @GeneratedValue
    private int id;
    @Column(name = "vedlegg_id")
    private String vedlegg;
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "vedlegg")
    private JPAInnsendingInnslag innslag;
    @Enumerated(STRING)
    @Column(name = "innsendingtype")
    private InnsendingType innsendingType;

    public InnsendingType getInnsendingType() {
        return innsendingType;
    }

    public void setInnsendingType(InnsendingType innsendingType) {
        this.innsendingType = innsendingType;
    }

    public JPAInnsendingVedlegg() {
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

    public JPAInnsendingInnslag getInnslag() {
        return innslag;
    }

    public void setInnslag(JPAInnsendingInnslag innslag) {
        this.innslag = innslag;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[id=" + id + ", vedlegg=" + vedlegg + ", innsendingType="
                + innsendingType + "]";
    }

}
