package no.nav.foreldrepenger.historikk.tjenester.innsending;

import static no.nav.foreldrepenger.historikk.config.TxConfiguration.JPA_TM;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(JPA_TM)
public class DittNavOppgave {

    private static final Logger LOG = LoggerFactory.getLogger(DittNavOppgave.class);

    private final JPADittNavOppgaverRepository dao;

    public DittNavOppgave(JPADittNavOppgaverRepository dao) {
        this.dao = dao;
    }

    public void opprett(InnsendingHendelse h) {
        var oppgave = new JPADittNavOppgave();
        oppgave.setFnr(h.getFnr());
        oppgave.setReferanseId(h.getReferanseId());
        oppgave.setSaksnr(h.getSaksnummer());
        dao.save(oppgave);
    }

    public boolean slett(String referanseId) {
        var oppgave = dao.findByReferanseId(referanseId);
        if (oppgave != null) {
            LOG.info("Sletter oppgave {}", referanseId);
            dao.delete(oppgave);
            LOG.info("Slettet oppgave {} OK", referanseId);
            return true;
        }
        LOG.info("Ingen oppgave å slette for {}", referanseId);
        return false;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[dao=" + dao + "]";
    }
}
