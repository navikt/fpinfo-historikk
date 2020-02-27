package no.nav.foreldrepenger.historikk.tjenester.innsending;

import static no.nav.foreldrepenger.historikk.config.TxConfiguration.JPA_TM;
import static no.nav.foreldrepenger.historikk.tjenester.felles.HistorikkInnslag.SORT_OPPRETTET_ASC;
import static no.nav.foreldrepenger.historikk.tjenester.innsending.InnsendingMapper.fraHendelse;
import static no.nav.foreldrepenger.historikk.tjenester.innsending.InnsendingMapper.tilInnslag;
import static no.nav.foreldrepenger.historikk.tjenester.innsending.JPAInnsendingSpec.harAktørId;
import static org.springframework.data.jpa.domain.Specification.where;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import no.nav.foreldrepenger.historikk.domain.AktørId;
import no.nav.foreldrepenger.historikk.tjenester.felles.IdempotentTjeneste;
import no.nav.foreldrepenger.historikk.tjenester.oppslag.Oppslag;

@Service
@Transactional(JPA_TM)
public class InnsendingTjeneste implements IdempotentTjeneste<InnsendingHendelse> {

    private static final Logger LOG = LoggerFactory.getLogger(InnsendingTjeneste.class);

    private final JPAInnsendingRepository dao;
    private final Oppslag oppslag;

    public InnsendingTjeneste(JPAInnsendingRepository dao, Oppslag oppslag) {
        this.dao = dao;
        this.oppslag = oppslag;
    }

    @Override
    public boolean lagre(InnsendingHendelse hendelse) {
        if (!erAlleredeLagret(hendelse.getReferanseId())) {
            LOG.info("Lagrer innsendingsinnslag fra {}", hendelse);
            dao.save(fraHendelse(hendelse));
            LOG.info("Lagret innsendingsinnslag OK");
            return true;
        } else {
            LOG.info("Innsendingsinnslag med referanseId {} er allerede lagret", hendelse.getReferanseId());
            return false;
        }
    }

    @Transactional(readOnly = true)
    public List<InnsendingInnslag> innsendinger() {
        return innsendinger(oppslag.aktørId());
    }

    public void fordel(InnsendingFordeltOgJournalførtHendelse h) {
        var eksisterende = dao.findByReferanseId(h.getForsendelseId());
        if (eksisterende != null && eksisterende.getSaksnr() == null && eksisterende.getJournalpostId() == null) {
            LOG.info("Oppdaterer innsendingsinnslag med saksnr og journalpostid");
            eksisterende.setSaksnr(h.getSaksnr());
            eksisterende.setJournalpostId(h.getJournalpostId());
            dao.save(eksisterende);
        } else {
            LOG.info("Ingenting å oppdatere fra hendelse");
        }
    }

    @Transactional(readOnly = true)
    public List<InnsendingInnslag> innsendinger(AktørId id) {
        LOG.info("Henter innsendingsinnslag for {}", id);
        var innslag = tilInnslag(dao.findAll(where(harAktørId(id)), SORT_OPPRETTET_ASC));
        LOG.info("Hentet innsendingsinnslag {}", innslag);
        return innslag;
    }

    @Override
    public boolean erAlleredeLagret(String referanseId) {
        return referanseId != null && dao.findByReferanseId(referanseId) != null;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[dao=" + dao + "]";
    }

}
