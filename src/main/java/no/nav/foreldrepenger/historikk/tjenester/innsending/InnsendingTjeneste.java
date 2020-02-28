package no.nav.foreldrepenger.historikk.tjenester.innsending;

import static no.nav.foreldrepenger.historikk.config.TxConfiguration.JPA_TM;
import static no.nav.foreldrepenger.historikk.tjenester.felles.HistorikkInnslag.SORT_OPPRETTET_ASC;
import static no.nav.foreldrepenger.historikk.tjenester.innsending.InnsendingMapper.tilInnslag;
import static no.nav.foreldrepenger.historikk.tjenester.innsending.JPAInnsendingSpec.harAktørId;
import static org.springframework.data.jpa.domain.Specification.where;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import no.nav.foreldrepenger.historikk.domain.AktørId;
import no.nav.foreldrepenger.historikk.tjenester.oppslag.Oppslag;

@Service
@Transactional(JPA_TM)
public class InnsendingTjeneste {

    private static final Logger LOG = LoggerFactory.getLogger(InnsendingTjeneste.class);

    private final JPAInnsendingRepository dao;
    private final Oppslag oppslag;

    public InnsendingTjeneste(JPAInnsendingRepository dao, Oppslag oppslag) {
        this.dao = dao;
        this.oppslag = oppslag;
    }

    public void lagreEllerOppdater(InnsendingHendelse h) {
        var eksisterende = dao.findByReferanseId(h.getReferanseId());
        if (eksisterende != null) {
            LOG.info("Oppdaterer innsendingsinnslag fra {}", h);
            dao.save(InnsendingMapper.oppdaterFra(h, eksisterende));
            LOG.info("Oppdaterte innsendingsinnslag OK");
        } else {
            LOG.info("Ingenting å oppdatere fra hendelse, insert istedet");
            dao.save(InnsendingMapper.nyFra(h));
            LOG.info("Insert fra hendelse OK");
        }
    }

    @Transactional(readOnly = true)
    public List<InnsendingInnslag> innsendinger() {
        return innsendinger(oppslag.aktørId());
    }

    public void fordel(InnsendingFordeltOgJournalførtHendelse h) {
        var eksisterende = dao.findByReferanseId(h.getForsendelseId());
        if (eksisterende != null) {
            if (eksisterende.getSaksnr() == null && eksisterende.getJournalpostId() == null) {
                LOG.info("Oppdaterer innsendingsinnslag med saksnr og journalpostid");
                dao.save(InnsendingMapper.oppdaterFra(h, eksisterende));
                LOG.info("Oppdaterer fra hendelse OK");
            } else {
                LOG.info("Eksisterende innslag er allerede komplett");
            }
        } else {
            LOG.info("Ingenting å oppdatere fra hendelse, insert istedet");
            dao.save(InnsendingMapper.nyFra(h));
            LOG.info("Insert fra hendelse OK");
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
    public String toString() {
        return getClass().getSimpleName() + "[dao=" + dao + "]";
    }
}
