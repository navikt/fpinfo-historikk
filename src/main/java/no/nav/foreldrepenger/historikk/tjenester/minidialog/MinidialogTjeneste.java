package no.nav.foreldrepenger.historikk.tjenester.minidialog;

import static java.util.stream.Collectors.toList;
import static no.nav.foreldrepenger.historikk.config.TxConfiguration.JPA_TM;
import static no.nav.foreldrepenger.historikk.tjenester.minidialog.MinidialogMapper.fraInnslag;
import static no.nav.foreldrepenger.historikk.tjenester.minidialog.dao.MinidialogSpec.erAktiv;
import static no.nav.foreldrepenger.historikk.tjenester.minidialog.dao.MinidialogSpec.erGyldig;
import static no.nav.foreldrepenger.historikk.tjenester.minidialog.dao.MinidialogSpec.gyldigErNull;
import static no.nav.foreldrepenger.historikk.tjenester.minidialog.dao.MinidialogSpec.harFnr;
import static org.springframework.data.jpa.domain.Specification.where;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import no.nav.foreldrepenger.historikk.domain.Fødselsnummer;
import no.nav.foreldrepenger.historikk.tjenester.innsending.Hendelse;
import no.nav.foreldrepenger.historikk.tjenester.minidialog.dao.JPAMinidialogInnslag;
import no.nav.foreldrepenger.historikk.tjenester.minidialog.dao.MinidialogRepository;
import no.nav.foreldrepenger.historikk.util.TokenUtil;

@Service
@Transactional(JPA_TM)
public class MinidialogTjeneste {

    private static final Logger LOG = LoggerFactory.getLogger(MinidialogTjeneste.class);

    private final MinidialogRepository dao;
    private final TokenUtil tokenUtil;

    public MinidialogTjeneste(MinidialogRepository dao, TokenUtil tokenUtil) {
        this.dao = dao;
        this.tokenUtil = tokenUtil;
    }

    public int deaktiverMinidialoger(Fødselsnummer fnr, Hendelse hendelse, String saksnr) {
        LOG.info("Deaktiverer minidialog(er) for {} etter hendelse {}", fnr, hendelse);
        if (hendelse.erEttersending()) {
            int n = dao.deaktiverSak(fnr, hendelse, saksnr);
            LOG.info("Deaktiverte {} minidialog(er) for sak {} etter hendelse {}", n, saksnr, hendelse);
            return n;
        }
        int n = dao.deaktiver(fnr, hendelse);
        LOG.info("Deaktiverte {} minidialog(er) etter hendelse {}", n, hendelse);
        return n;
    }

    public void lagre(MinidialogHendelse m) {
        LOG.info("Lagrer minidialog {}", m);
        dao.save(fraInnslag(m));
        LOG.info("Lagret minidialog OK");
    }

    @Transactional(readOnly = true)
    public List<MinidialogHendelse> hentAktiveDialoger() {
        return hentAktiveDialoger(tokenUtil.autentisertFNR());
    }

    @Transactional(readOnly = true)
    public List<MinidialogHendelse> hentAktiveDialoger(Fødselsnummer fnr) {
        LOG.info("Henter aktive dialoger for {}", fnr);
        List<MinidialogHendelse> dialoger = mapAndCollect(
                dao.findAll(
                        where(harFnr(fnr)
                                .and((erGyldig().or(gyldigErNull())))
                                .and(erAktiv()))));
        LOG.info("Hentet {} dialog(er) {}", dialoger.size(), dialoger);
        return dialoger;
    }

    private static List<MinidialogHendelse> mapAndCollect(List<JPAMinidialogInnslag> innslag) {
        return innslag
                .stream()
                .map(MinidialogMapper::tilInnslag)
                .collect(toList());
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [dao=" + dao + ", tokenUtil=" + tokenUtil + "]";
    }

}
