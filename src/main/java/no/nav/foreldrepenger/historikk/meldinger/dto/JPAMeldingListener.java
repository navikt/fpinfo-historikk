package no.nav.foreldrepenger.historikk.meldinger.dto;

import javax.persistence.PostLoad;
import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;

public class JPAMeldingListener {
    @PrePersist
    public void userPrePersist(JPAMelding ob) {
        System.out.println("Listening Melding Pre Persist : " + ob);
    }

    @PostPersist
    public void userPostPersist(JPAMelding ob) {
        System.out.println("Listening Melding Post Persist : " + ob);
    }

    @PostLoad
    public void userPostLoad(JPAMelding ob) {
        System.out.println("Listening Melding Post Load : " + ob);
    }

    @PreUpdate
    public void userPreUpdate(JPAMelding ob) {
        System.out.println("Listening Melding Pre Update : " + ob);
    }

    @PostUpdate
    public void userPostUpdate(JPAMelding ob) {
        System.out.println("Listening Melding Post Update : " + ob);
    }

    @PreRemove
    public void userPreRemove(JPAMelding ob) {
        System.out.println("Listening Melding Pre Remove : " + ob);
    }

    @PostRemove
    public void userPostRemove(JPAMelding ob) {
        System.out.println("Listening Melding Post Remove : " + ob);
    }
}
