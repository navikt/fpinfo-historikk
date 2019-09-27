package no.nav.foreldrepenger.historikk.tjenester.minidialog;

import static java.util.stream.Collectors.toList;
import static no.nav.foreldrepenger.historikk.config.TxConfiguration.JPA_TM;
import static no.nav.foreldrepenger.historikk.tjenester.felles.HistorikkInnslag.SORT_OPPRETTET_ASC;
import static no.nav.foreldrepenger.historikk.tjenester.minidialog.MinidialogMapper.fraHendelse;
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
import no.nav.foreldrepenger.historikk.tjenester.felles.Hendelse;
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

    public int deaktiverMinidialoger(Hendelse hendelse) {
        if (hendelse.getHendelse().erEttersending()) {
            int n = dao.deaktiverSak(hendelse.getFnr(), hendelse.getSaksNr());
            LOG.info("Deaktiverte {} minidialog{} for sak {} etter hendelse {}", n, flertall(n), hendelse.getSaksNr(),
                    hendelse.getHendelse());
            return n;
        }
        LOG.info("Ingen deaktivering for {} og hendelse {}", hendelse.getFnr(), hendelse.getHendelse());
        return 0;
    }

    public void lagre(MinidialogHendelse hendelse, String journalPostId) {
        if (skalLagre(hendelse)) {
            LOG.info("Lagrer minidialog {}", hendelse);
            dao.save(fraHendelse(hendelse, journalPostId));
            LOG.info("Lagret minidialog OK");
            deaktiverMinidialoger(hendelse);
        } else {
            LOG.info("Hendelse med referanseId {} er allerede lagret", hendelse.getReferanseId());
        }
    }

    private boolean skalLagre(MinidialogHendelse hendelse) {
        String referanseId = hendelse.getReferanseId();
        return referanseId == null || dao.findByReferanseId(referanseId) == null;
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
        return hentAktiveDialogSpørsmål(tokenUtil.autentisertFNR());
    }

    @Transactional(readOnly = true)
    public List<MinidialogInnslag> hentAktiveDialogSpørsmål(Fødselsnummer fnr) {
        LOG.info("Henter aktive spørsmål for {}", fnr);
        List<MinidialogInnslag> dialoger = mapAndCollect(
                dao.findAll(where(spec(fnr, true).and(erSpørsmål())), SORT_OPPRETTET_ASC));
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
