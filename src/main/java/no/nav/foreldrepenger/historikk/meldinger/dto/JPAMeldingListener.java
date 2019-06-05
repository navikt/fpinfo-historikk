package no.nav.foreldrepenger.historikk.meldinger.dto;

import javax.persistence.PostLoad;
import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JPAMeldingListener {

    private static final Logger LOG = LoggerFactory.getLogger(JPAMeldingListener.class);

    @PrePersist
    public void userPrePersist(JPAMelding ob) {
        LOG.info("Listening Melding Pre Persist : " + ob);
    }

    @PostPersist
    public void userPostPersist(JPAMelding ob) {
        LOG.info("Listening Melding Post Persist : " + ob);
    }

    @PostLoad
    public void userPostLoad(JPAMelding ob) {
        LOG.info("Listening Melding Post Load : " + ob);
    }

    @PreUpdate
    public void userPreUpdate(JPAMelding ob) {
        LOG.info("Listening Melding Pre Update : " + ob);
    }

    @PostUpdate
    public void userPostUpdate(JPAMelding ob) {
        LOG.info("Listening Melding Post Update : " + ob);
    }

    @PreRemove
    public void userPreRemove(JPAMelding ob) {
        LOG.info("Listening Melding Pre Remove : " + ob);
    }

    @PostRemove
    public void userPostRemove(JPAMelding ob) {
        LOG.info("Listening Melding Post Remove : " + ob);
    }
}
