package no.nav.foreldrepenger.historikk.tjenester.minidialog;

import static java.util.stream.Collectors.toList;
import static no.nav.foreldrepenger.historikk.config.TxConfiguration.JPA_TM;
import static no.nav.foreldrepenger.historikk.tjenester.felles.HistorikkInnslag.SORT_OPPRETTET_ASC;
import static no.nav.foreldrepenger.historikk.tjenester.minidialog.MinidialogMapper.fraInnslag;
import static no.nav.foreldrepenger.historikk.tjenester.minidialog.dao.JPAMinidialogSpec.erAktiv;
import static no.nav.foreldrepenger.historikk.tjenester.minidialog.dao.JPAMinidialogSpec.erGyldig;
import static no.nav.foreldrepenger.historikk.tjenester.minidialog.dao.JPAMinidialogSpec.erSpørsmål;
import static no.nav.foreldrepenger.historikk.tjenester.minidialog.dao.JPAMinidialogSpec.gyldigErNull;
import static no.nav.foreldrepenger.historikk.tjenester.minidialog.dao.JPAMinidialogSpec.harFnr;
import static no.nav.foreldrepenger.historikk.util.StringUtil.flertall;
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
            LOG.info("Deaktiverte {} minidialog{} for sak {} etter hendelse {}", n, flertall(n), saksnr,
                    hendelse);
            return n;
        }
        int n = dao.deaktiver(fnr);
        LOG.info("Deaktiverte {} minidialog{} etter hendelse {}", n, flertall(n), hendelse);
        return n;
    }

    public void lagre(MinidialogHendelse m, String journalPostId) {
        LOG.info("Lagrer minidialog {}", m);
        dao.save(fraInnslag(m, journalPostId));
        LOG.info("Lagret minidialog OK");
        deaktiverMinidialoger(m.getFnr(), m.getHendelse(), m.getSaksNr());
    }

    @Transactional(readOnly = true)
    public List<MinidialogInnslag> hentDialoger(boolean activeOnly) {
        return hentDialoger(tokenUtil.autentisertFNR(), activeOnly);
    }

    @Transactional(readOnly = true)
    public List<MinidialogInnslag> hentDialoger(Fødselsnummer fnr, boolean activeOnly) {
        LOG.info("Henter dialoger for {} og activeOnly={}", fnr, activeOnly);
        List<MinidialogInnslag> dialoger = mapAndCollect(dao.findAll(where(spec(fnr, activeOnly)), SORT_OPPRETTET_ASC));
        LOG.info("Hentet {} dialog{} ({})", dialoger.size(), flertall(dialoger), dialoger);
        return dialoger;
    }

    @Transactional(readOnly = true)
    public List<MinidialogInnslag> hentAktiveDialogSpørsmål() {
        LOG.info("Henter spørsmål for {}", tokenUtil.autentisertFNR());
        List<MinidialogInnslag> dialoger = mapAndCollect(
                dao.findAll(where(spec(tokenUtil.autentisertFNR(), true).and(erSpørsmål())), SORT_OPPRETTET_ASC));
        LOG.info("Hentet {} dialogspørsmål ({})", dialoger.size(), dialoger);
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
