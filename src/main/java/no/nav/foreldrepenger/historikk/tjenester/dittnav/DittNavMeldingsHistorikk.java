package no.nav.foreldrepenger.historikk.tjenester.dittnav;

import static no.nav.foreldrepenger.historikk.config.JpaTxConfiguration.JPA_TM;
import static no.nav.foreldrepenger.historikk.tjenester.dittnav.JPADittNavOppgave.NotifikasjonType.BESKJED;
import static no.nav.foreldrepenger.historikk.tjenester.dittnav.JPADittNavOppgave.NotifikasjonType.OPPGAVE;
import static no.nav.foreldrepenger.historikk.tjenester.dittnav.JPADittNavOppgaverSpec.*;
import static org.springframework.data.jpa.domain.Specification.where;

import no.nav.foreldrepenger.historikk.tjenester.dittnav.JPADittNavOppgave.NotifikasjonType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import no.nav.foreldrepenger.historikk.domain.Fødselsnummer;

import java.util.List;

@Service
@Transactional(JPA_TM)
public class DittNavMeldingsHistorikk {

    private static final Logger LOG = LoggerFactory.getLogger(DittNavMeldingsHistorikk.class);

    private final JPADittNavOppgaverRepository dao;

    public DittNavMeldingsHistorikk(JPADittNavOppgaverRepository dao) {
        this.dao = dao;
    }

    public void opprettOppgave(Fødselsnummer fnr, String grupperingsId, String internReferanse, String eksternReferanse) {
        opprett(fnr, grupperingsId, internReferanse, eksternReferanse, OPPGAVE);
    }

    public void opprettBeskjed(Fødselsnummer fnr, String grupperingsId, String internReferanse, String eksternReferanse) {
        opprett(fnr, grupperingsId, internReferanse, eksternReferanse, BESKJED);
    }

    public List<JPADittNavOppgave> hentAktivOppgave(String internReferanseId) {
        return dao.findAll(where(harReferanseId(internReferanseId).and(erAktiv()).and(erOppgave())));
    }

    public boolean erOpprettetOppgave(String referanseId) {
        return dao.existsByInternReferanseIdIgnoreCaseAndType(referanseId, OPPGAVE);
    }

    public boolean erOpprettetBeskjed(String referanseId) {
        return dao.existsByInternReferanseIdIgnoreCaseAndType(referanseId, BESKJED);
    }

    public JPADittNavOppgave avslutt(JPADittNavOppgave oppgave) {
        oppgave.setSendtDoneMelding(true);
        return dao.save(oppgave);
    }

    private void opprett(Fødselsnummer fnr, String grupperingsId, String internReferanse, String eksternReferanse, NotifikasjonType type) {
        var ny = new JPADittNavOppgave();
        ny.setFnr(fnr);
        ny.setType(type);
        ny.setGrupperingsId(grupperingsId);
        ny.setInternReferanseId(internReferanse);
        ny.setEksternReferanseId(eksternReferanse);
        var saved = dao.save(ny);
        LOG.info("Lagret DittNav {} i db {}", type, saved);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[dao=" + dao + "]";
    }
}
