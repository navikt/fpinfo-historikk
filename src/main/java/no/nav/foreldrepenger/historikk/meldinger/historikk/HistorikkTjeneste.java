package no.nav.foreldrepenger.historikk.meldinger.historikk;

import static no.nav.foreldrepenger.historikk.config.TxConfiguration.JPA_TM;
import static no.nav.foreldrepenger.historikk.meldinger.historikk.HistorikkMapper.fraEvent;
import static no.nav.foreldrepenger.historikk.meldinger.historikk.HistorikkMapper.konverterFra;
import static no.nav.foreldrepenger.historikk.meldinger.historikk.dao.HistorikkSpec.erEtter;
import static no.nav.foreldrepenger.historikk.meldinger.historikk.dao.HistorikkSpec.harAktør;
import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.data.jpa.domain.Specification.where;

import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import no.nav.foreldrepenger.historikk.domain.AktørId;
import no.nav.foreldrepenger.historikk.meldinger.historikk.dao.HistorikkRepository;
import no.nav.foreldrepenger.historikk.meldinger.innsending.InnsendingEvent;
import no.nav.foreldrepenger.historikk.meldinger.oppslag.OppslagTjeneste;

@Service
@Transactional(JPA_TM)
public class HistorikkTjeneste {

    private static final Sort SORT = new Sort(ASC, "opprettet");
    private static final Logger LOG = LoggerFactory.getLogger(HistorikkTjeneste.class);

    private final HistorikkRepository dao;
    private final OppslagTjeneste oppslag;

    public HistorikkTjeneste(HistorikkRepository dao, OppslagTjeneste oppslag) {
        this.dao = dao;
        this.oppslag = oppslag;
    }

    public void lagre(InnsendingEvent event) {
        if (event != null) {
            LOG.info("Lagrer historikkinnslag fra {}", event);
            dao.save(fraEvent(event));
            LOG.info("Lagret historikkinnslag fra OK ");
        }
    }

    @Transactional(readOnly = true)
    public List<HistorikkInnslag> hentMinHistorikk() {
        LOG.info("Henter historikkinnslag");
        List<HistorikkInnslag> innslag = konverterFra(dao.findAll(where(harAktør(oppslag.hentAktørId())), SORT));
        LOG.info("Hentet historikkinnslag {}", innslag);
        return innslag;
    }

    @Transactional(readOnly = true)
    public List<HistorikkInnslag> hentHistorikk(AktørId aktørId) {
        LOG.info("Hentet historikkinnslag for {}", aktørId);
        List<HistorikkInnslag> innslag = konverterFra(dao.findAll(where(harAktør(aktørId)), SORT));
        LOG.info("Hentet historikkinnslag {}", innslag);
        return innslag;
    }

    @Transactional(readOnly = true)
    public List<HistorikkInnslag> hentHistorikkFra(AktørId aktørId, LocalDate dato) {
        LOG.info("Hentet historikkinnslag fra {}", dato);
        List<HistorikkInnslag> innslag = konverterFra(dao.findAll(where(harAktør(aktørId)).and(erEtter(dato)), SORT));
        LOG.info("Hentet historikkinnslag {}", innslag);
        return innslag;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [dao=" + dao + ", oppslag=" + oppslag + "]";
    }
}
