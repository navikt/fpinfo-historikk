package no.nav.foreldrepenger.historikk.tjenester.dittnav;

import static no.nav.foreldrepenger.historikk.config.JpaTxConfiguration.JPA_TM;
import static no.nav.foreldrepenger.historikk.tjenester.dittnav.JPADittNavOppgave.NotifikasjonType.BESKJED;
import static no.nav.foreldrepenger.historikk.tjenester.dittnav.JPADittNavOppgave.NotifikasjonType.OPPGAVE;

import no.nav.foreldrepenger.historikk.tjenester.dittnav.JPADittNavOppgave.NotifikasjonType;
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

    public void opprettOppgave(Fødselsnummer fnr, String internReferanse, String eksternReferanse) {
        opprett(fnr, internReferanse, eksternReferanse, OPPGAVE);
    }

    public void opprettBeskjed(Fødselsnummer fnr, String internReferanse, String eksternReferanse) {
        opprett(fnr, internReferanse, eksternReferanse, BESKJED);
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

    private void opprett(Fødselsnummer fnr, String internReferanse, String eksternReferanse, NotifikasjonType type) {
        var ny = new JPADittNavOppgave();
        ny.setFnr(fnr);
        ny.setType(type);
        ny.setInternReferanseId(internReferanse);
        ny.setEksternReferanseId(eksternReferanse);
        var prefix = type.equals(BESKJED) ? "B" : "C";
        ny.setReferanseId(prefix + eksternReferanse); // expand-contract
        var saved = dao.save(ny);
        LOG.info("Lagret DittNav {} i lokal db ok {}", type, saved);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[dao=" + dao + "]";
    }
}
