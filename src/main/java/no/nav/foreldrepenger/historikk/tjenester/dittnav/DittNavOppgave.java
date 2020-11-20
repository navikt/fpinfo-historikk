package no.nav.foreldrepenger.historikk.tjenester.dittnav;

import static no.nav.foreldrepenger.historikk.config.TxConfiguration.JPA_TM;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import no.nav.foreldrepenger.historikk.domain.Fødselsnummer;
import no.nav.foreldrepenger.historikk.tjenester.felles.HendelseType;

@Service
@Transactional(JPA_TM)
public class DittNavOppgave {

    private static final Logger LOG = LoggerFactory.getLogger(DittNavOppgave.class);

    private final JPADittNavOppgaverRepository dao;

    public DittNavOppgave(JPADittNavOppgaverRepository dao) {
        this.dao = dao;
    }

    public void opprett(Fødselsnummer fnr, String referanseId, String saksnr, HendelseType hendelse) {
        LOG.info("Oppretter info om  melding til Ditt Nav i DB {} {} {}", fnr, referanseId, saksnr);
        var ny = new JPADittNavOppgave();
        ny.setFnr(fnr);
        ny.setReferanseId(referanseId);
        ny.setSaksnr(saksnr);
        ny.setHendelse(hendelse);
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

    public boolean erOpprettet(String saksnr, HendelseType hendelse) {
        return dao.findBySaksnrAndHendelse(saksnr, hendelse) != null;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[dao=" + dao + "]";
    }
}
