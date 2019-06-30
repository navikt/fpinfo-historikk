package no.nav.foreldrepenger.historikk.tjenester.minidialog;

import static java.util.stream.Collectors.toList;
import static no.nav.foreldrepenger.historikk.config.TxConfiguration.JPA_TM;
import static no.nav.foreldrepenger.historikk.tjenester.minidialog.MinidialogMapper.fraInnslag;
import static no.nav.foreldrepenger.historikk.tjenester.minidialog.dao.MinidialogSpec.erAktiv;
import static no.nav.foreldrepenger.historikk.tjenester.minidialog.dao.MinidialogSpec.erGyldig;
import static no.nav.foreldrepenger.historikk.tjenester.minidialog.dao.MinidialogSpec.harAktør;
import static org.springframework.data.jpa.domain.Specification.where;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import no.nav.foreldrepenger.historikk.domain.AktørId;
import no.nav.foreldrepenger.historikk.tjenester.innsending.Hendelse;
import no.nav.foreldrepenger.historikk.tjenester.minidialog.dao.JPAMinidialogInnslag;
import no.nav.foreldrepenger.historikk.tjenester.minidialog.dao.MinidialogRepository;
import no.nav.foreldrepenger.historikk.tjenester.oppslag.OppslagTjeneste;

@Service
@Transactional(JPA_TM)
public class MinidialogTjeneste {

    private static final Logger LOG = LoggerFactory.getLogger(MinidialogTjeneste.class);

    private final MinidialogRepository dao;
    private final OppslagTjeneste oppslag;

    public MinidialogTjeneste(MinidialogRepository dao, OppslagTjeneste oppslag) {
        this.dao = dao;
        this.oppslag = oppslag;
    }

    public int deaktiverMinidialoger(String aktørId, Hendelse hendelse, String saksnr) {
        LOG.info("Deaktiverer minidialoger for {} etter hendelse {}", aktørId, hendelse);
        if (hendelse.erEttersending()) {
            int n = dao.deaktiverSak(aktørId, hendelse, saksnr);
            LOG.info("Deaktiverte {} minidialoger for sak {}", hendelse, saksnr);
            return n;
        }
        int n = dao.deaktiver(aktørId, hendelse);
        LOG.info("Deaktiverte {} minidialoger for {}", n, hendelse);
        return n;
    }

    public void lagre(MinidialogInnslag m) {
        LOG.info("Lagrer minidialog {}", m);
        dao.save(fraInnslag(m));
        LOG.info("Lagret minidialog OK");
    }

    @Transactional(readOnly = true)
    public List<MinidialogInnslag> hentAktiveDialogerForAktør(AktørId aktørId) {
        return hentDialoger(aktørId);
    }

    @Transactional(readOnly = true)
    public List<MinidialogInnslag> hentMineAktiveDialoger() {
        LOG.info("Hentet aktive dialoger");
        List<MinidialogInnslag> dialoger = hentDialoger(oppslag.hentAktørId());
        LOG.info("Hentet dialoger {}", dialoger);
        return dialoger;

    }

    int deaktiver(String aktørId, Hendelse type) {
        return dao.deaktiver(aktørId, type);
    }

    private List<MinidialogInnslag> hentDialoger(AktørId aktørId) {
        return mapAndCollect(
                dao.findAll(
                        where(harAktør(aktørId)
                                .and(erGyldig())
                                .and(erAktiv()))));
    }

    private static List<MinidialogInnslag> mapAndCollect(List<JPAMinidialogInnslag> innslag) {
        return innslag
                .stream()
                .map(MinidialogMapper::tilInnslag)
                .collect(toList());
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [dao=" + dao + ", oppslag=" + oppslag + "]";
    }

}
