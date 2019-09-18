package no.nav.foreldrepenger.historikk.tjenester.minidialog;

import static java.util.stream.Collectors.toList;
import static no.nav.foreldrepenger.historikk.config.TxConfiguration.JPA_TM;
import static no.nav.foreldrepenger.historikk.tjenester.minidialog.MinidialogMapper.fraInnslag;
import static no.nav.foreldrepenger.historikk.tjenester.minidialog.dao.JPAMinidialogSpec.erAktiv;
import static no.nav.foreldrepenger.historikk.tjenester.minidialog.dao.JPAMinidialogSpec.erGyldig;
import static no.nav.foreldrepenger.historikk.tjenester.minidialog.dao.JPAMinidialogSpec.gyldigErNull;
import static no.nav.foreldrepenger.historikk.tjenester.minidialog.dao.JPAMinidialogSpec.harFnr;
import static no.nav.foreldrepenger.historikk.util.StringUtil.endelse;
import static org.springframework.data.jpa.domain.Specification.where;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import no.nav.foreldrepenger.historikk.domain.Fødselsnummer;
import no.nav.foreldrepenger.historikk.tjenester.felles.HendelseType;
import no.nav.foreldrepenger.historikk.tjenester.minidialog.dao.JPAMinidialogInnslag;
import no.nav.foreldrepenger.historikk.tjenester.minidialog.dao.JPAMinidialogRepository;
import no.nav.foreldrepenger.historikk.util.TokenUtil;

@Service
@Transactional(JPA_TM)
public class MinidialogTjeneste {

    private static final Logger LOG = LoggerFactory.getLogger(MinidialogTjeneste.class);

    private final JPAMinidialogRepository dao;
    private final TokenUtil tokenUtil;

    public MinidialogTjeneste(JPAMinidialogRepository dao, TokenUtil tokenUtil) {
        this.dao = dao;
        this.tokenUtil = tokenUtil;
    }

    public int deaktiverMinidialoger(Fødselsnummer fnr, HendelseType hendelse, String saksnr) {
        LOG.info("Deaktiverer minidialog(er) for {} etter hendelse {}", fnr, hendelse);
        if (hendelse.erEttersending()) {
            int n = dao.deaktiverSak(fnr, saksnr);
            LOG.info("Deaktiverte {} minidialog(er) for sak {} etter hendelse {}", n, saksnr, hendelse);
            return n;
        }
        int n = dao.deaktiver(fnr);
        LOG.info("Deaktiverte {} minidialog(er) etter hendelse {}", n, hendelse);
        return n;
    }

    public void lagre(MinidialogHendelse m, String journalPostId) {
        LOG.info("Lagrer minidialog {}", m);
        dao.save(fraInnslag(m, journalPostId));
        LOG.info("Lagret minidialog OK");
    }

    @Transactional(readOnly = true)
    public List<MinidialogInnslag> hentDialoger(boolean activeOnly) {
        return hentDialoger(tokenUtil.autentisertFNR(), activeOnly);
    }

    @Transactional(readOnly = true)
    public List<MinidialogInnslag> hentDialoger(Fødselsnummer fnr, boolean activeOnly) {
        LOG.info("Henter dialoger for {} og aktiv={}", fnr, activeOnly);
        List<MinidialogInnslag> dialoger = mapAndCollect(dao.findAll(where(spec(fnr, activeOnly))));
        LOG.info("Hentet {} dialog{} ({})", dialoger.size(), endelse(dialoger), dialoger);
        return dialoger;
    }

    private Specification<JPAMinidialogInnslag> spec(Fødselsnummer fnr, boolean activeOnly) {
        Specification<JPAMinidialogInnslag> spec = harFnr(fnr);
        return activeOnly ? spec
                .and((erGyldig().or(gyldigErNull())))
                .and(erAktiv()) : spec;
    }

    private static List<MinidialogInnslag> mapAndCollect(List<JPAMinidialogInnslag> innslag) {
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
