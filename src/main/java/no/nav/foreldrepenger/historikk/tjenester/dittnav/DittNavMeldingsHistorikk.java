package no.nav.foreldrepenger.historikk.tjenester.dittnav;

import static no.nav.foreldrepenger.historikk.config.TxConfiguration.JPA_TM;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import no.nav.foreldrepenger.historikk.domain.Fødselsnummer;

@Service
@Transactional(JPA_TM)
public class DittNavMeldingsHistorikk {

    private static final Logger LOG = LoggerFactory.getLogger(DittNavMeldingsHistorikk.class);

    private final JPADittNavOppgaverRepository dao;

    public DittNavMeldingsHistorikk(JPADittNavOppgaverRepository dao) {
        this.dao = dao;
    }

    public void opprett(Fødselsnummer fnr, String referanseId) {
        LOG.info("Oppretter info om  melding til Ditt Nav i DB {} {}", fnr, referanseId);
        var ny = new JPADittNavOppgave();
        ny.setFnr(fnr);
        ny.setReferanseId(referanseId);
        var saved = dao.save(ny);
        LOG.info("Opprettet info om  melding til Ditt Nav i DB OK {}", saved);
    }

    public boolean slett(String referanseId) {
        var oppgave = dao.findByReferanseId(referanseId);
        if (oppgave != null) {
            LOG.info("Sletter oppgave fra DB {}", referanseId);
            dao.delete(oppgave);
            LOG.info("Slettet oppgave {} fra DB OK", referanseId);
            return true;
        }
        LOG.info("Ingen oppgave å slette for {}", referanseId);
        return false;
    }

    public boolean erOpprettet(String referanseId) {
        return dao.findByReferanseId(referanseId) != null;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[dao=" + dao + "]";
    }
}
