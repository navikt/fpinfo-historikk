package no.nav.foreldrepenger.historikk.tjenester.dittnav;

import static no.nav.foreldrepenger.historikk.config.JpaTxConfiguration.JPA_TM;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import no.nav.foreldrepenger.historikk.domain.Fødselsnummer;

import java.util.Optional;

@Service
@Transactional(JPA_TM)
public class DittNavMeldingsHistorikk {

    private static final Logger LOG = LoggerFactory.getLogger(DittNavMeldingsHistorikk.class);

    private final JPADittNavOppgaverRepository dao;

    public DittNavMeldingsHistorikk(JPADittNavOppgaverRepository dao) {
        this.dao = dao;
    }

    public void opprett(Fødselsnummer fnr, String referanseId) {
        LOG.info("Oppretter info om  melding/oppgave til Ditt Nav i DB {} {}", fnr, referanseId);
        var ny = new JPADittNavOppgave();
        ny.setFnr(fnr);
        ny.setReferanseId(referanseId);
        var saved = dao.save(ny);
        LOG.info("Opprettet info om  melding/oppgave til Ditt Nav i DB OK {}", saved);
    }

    public boolean slett(String referanseId) {
        var oppgave = dao.findByReferanseIdIgnoreCase(referanseId);
        if (oppgave != null) {
            LOG.info("Sletter melding/oppgave fra DB {}", referanseId);
            dao.delete(oppgave);
            LOG.info("Slettet melding/oppgave {} fra DB OK", referanseId);
            return true;
        }
        LOG.info("Ingen oppgave/melding å slette for {}", referanseId);
        return false;
    }

    public boolean erOpprettet(String referanseId) {
        return dao.existsByReferanseIdIgnoreCase(referanseId);
    }

    public JPADittNavOppgave hentOppgave(String referanseIdA, String referanseIdB) {
        return Optional.ofNullable(dao.findByReferanseIdIgnoreCase(referanseIdA))
            .orElseGet(() -> dao.findByReferanseIdIgnoreCase(referanseIdB));
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[dao=" + dao + "]";
    }
}
