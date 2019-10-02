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
import static no.nav.foreldrepenger.historikk.util.StreamUtil.safeStream;
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
import no.nav.foreldrepenger.historikk.tjenester.felles.IdempotentTjeneste;
import no.nav.foreldrepenger.historikk.tjenester.minidialog.dao.JPAMinidialogInnslag;
import no.nav.foreldrepenger.historikk.tjenester.minidialog.dao.JPAMinidialogRepository;
import no.nav.foreldrepenger.historikk.util.TokenUtil;

@Service
@Transactional(JPA_TM)
public class MinidialogTjeneste implements IdempotentTjeneste<MinidialogHendelse> {

    private static final Logger LOG = LoggerFactory.getLogger(MinidialogTjeneste.class);

    private final JPAMinidialogRepository dao;
    private final TokenUtil tokenUtil;

    public MinidialogTjeneste(JPAMinidialogRepository dao, TokenUtil tokenUtil) {
        this.dao = dao;
        this.tokenUtil = tokenUtil;
    }

    public int deaktiver(Hendelse hendelse) {
        if (hendelse.erEttersending() && hendelse.getReferanseId() != null) {
            int n = dao.deaktiver(hendelse.getFnr(), hendelse.getReferanseId());
            LOG.info("Deaktiverte {} minidialog{} for referanseId {} etter hendelse {}", n, flertall(n),
                    hendelse.getReferanseId(),
                    hendelse.getHendelse());
            return n;
        }
        LOG.info("Ingen deaktivering for {} og hendelse {}", hendelse.getFnr(), hendelse.getHendelse());
        return 0;
    }

    @Override
    public void lagre(MinidialogHendelse hendelse) {
        lagre(hendelse, null);
    }

    public void lagre(MinidialogHendelse hendelse, String journalPostId) {
        if (skalLagre(hendelse)) {
            LOG.info("Lagrer minidialog {}", hendelse);
            dao.save(fraHendelse(hendelse, journalPostId));
            LOG.info("Lagret minidialog OK");
            deaktiver(hendelse);
        } else {
            LOG.info("Hendelse med referanseId {} er allerede lagret", hendelse.getReferanseId());
        }
    }

    @Transactional(readOnly = true)
    public List<MinidialogInnslag> dialoger(boolean activeOnly) {
        return dialoger(tokenUtil.autentisertFNR(), activeOnly);
    }

    @Transactional(readOnly = true)
    public List<MinidialogInnslag> dialoger(Fødselsnummer fnr, boolean activeOnly) {
        LOG.info("Henter dialoger for {} og activeOnly={}", fnr, activeOnly);
        return tilInnslag(dao.findAll(where(spec(fnr, activeOnly)), SORT_OPPRETTET_ASC));
    }

    @Transactional(readOnly = true)
    public List<MinidialogInnslag> aktive() {
        return aktive(tokenUtil.autentisertFNR());
    }

    @Transactional(readOnly = true)
    public List<MinidialogInnslag> aktive(Fødselsnummer fnr) {
        LOG.info("Henter aktive dialoginnslag for {}", fnr);
        return tilInnslag(
                dao.findAll(where(spec(fnr, true).and(erSpørsmål())), SORT_OPPRETTET_ASC));
    }

    private static Specification<JPAMinidialogInnslag> spec(Fødselsnummer fnr, boolean activeOnly) {
        var spec = harFnr(fnr);
        return activeOnly ? spec
                .and((erGyldig().or(gyldigErNull())))
                .and(erAktiv()) : spec;
    }

    private static List<MinidialogInnslag> tilInnslag(List<JPAMinidialogInnslag> innslag) {
        var i = safeStream(innslag)
                .map(MinidialogMapper::tilInnslag)
                .collect(toList());
        LOG.info("Hentet {} dialog{} ({})", i.size(), flertall(i), i);
        return i;

    }

    @Override
    public boolean erAlleredeLagret(String referanseId) {
        return referanseId != null && dao.findByReferanseId(referanseId) != null;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [dao=" + dao + ", tokenUtil=" + tokenUtil + "]";
    }
}
