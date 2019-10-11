package no.nav.foreldrepenger.historikk.tjenester.minidialog;

import static java.util.stream.Collectors.toList;
import static no.nav.foreldrepenger.historikk.config.TxConfiguration.JPA_TM;
import static no.nav.foreldrepenger.historikk.tjenester.felles.HendelseType.TILBAKEKREVING_SVAR;
import static no.nav.foreldrepenger.historikk.tjenester.felles.HistorikkInnslag.SORT_OPPRETTET_ASC;
import static no.nav.foreldrepenger.historikk.tjenester.minidialog.JPAMinidialogSpec.erAktiv;
import static no.nav.foreldrepenger.historikk.tjenester.minidialog.JPAMinidialogSpec.erGyldig;
import static no.nav.foreldrepenger.historikk.tjenester.minidialog.JPAMinidialogSpec.erSpørsmål;
import static no.nav.foreldrepenger.historikk.tjenester.minidialog.JPAMinidialogSpec.gyldigErNull;
import static no.nav.foreldrepenger.historikk.tjenester.minidialog.JPAMinidialogSpec.harAktørId;
import static no.nav.foreldrepenger.historikk.tjenester.minidialog.MinidialogMapper.fraHendelse;
import static no.nav.foreldrepenger.historikk.util.StreamUtil.safeStream;
import static no.nav.foreldrepenger.historikk.util.StringUtil.flertall;
import static org.springframework.data.jpa.domain.Specification.where;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import no.nav.foreldrepenger.historikk.domain.AktørId;
import no.nav.foreldrepenger.historikk.tjenester.felles.IdempotentTjeneste;
import no.nav.foreldrepenger.historikk.tjenester.oppslag.OppslagTjeneste;

@Service
@Transactional(JPA_TM)
public class MinidialogTjeneste implements IdempotentTjeneste<MinidialogHendelse> {

    private static final Logger LOG = LoggerFactory.getLogger(MinidialogTjeneste.class);

    private final JPAMinidialogRepository dao;
    private final OppslagTjeneste oppslag;

    public MinidialogTjeneste(JPAMinidialogRepository dao, OppslagTjeneste oppslag) {
        this.dao = dao;
        this.oppslag = oppslag;
    }

    public void deaktiver(AktørId aktørId, String dialogId) {
        int n = dao.deaktiver(aktørId, dialogId);
        LOG.info("Deaktiverte {} minidialog{} for aktør {} og  dialogId {} etter hendelse {}", n, flertall(n),
                aktørId, dialogId);
    }

    @Override
    public boolean lagre(MinidialogHendelse h) {
        if (!erAlleredeLagret(h.getDialogId())) {
            LOG.info("Lagrer minidialog {}", h);
            dao.save(fraHendelse(h));
            LOG.info("Lagret minidialog OK");
            if (TILBAKEKREVING_SVAR.equals(h.getHendelseType())) {
                deaktiver(h.getAktørId(), h.getDialogId());
            }
            return true;
        } else {
            LOG.info("Hendelse med dialogId {} er allerede lagret", h.getDialogId());
            return false;
        }
    }

    @Transactional(readOnly = true)
    public List<MinidialogInnslag> dialoger(boolean activeOnly) {
        return dialoger(oppslag.aktørId(), activeOnly);
    }

    @Transactional(readOnly = true)
    public List<MinidialogInnslag> dialoger(AktørId aktørId, boolean activeOnly) {
        LOG.info("Henter dialoginnslag for {} og activeOnly={}", aktørId, activeOnly);
        return tilInnslag(dao.findAll(where(spec(aktørId, activeOnly)), SORT_OPPRETTET_ASC));
    }

    @Transactional(readOnly = true)
    public List<MinidialogInnslag> aktive() {
        return aktive(oppslag.aktørId());
    }

    @Transactional(readOnly = true)
    public List<MinidialogInnslag> aktive(AktørId aktørId) {
        LOG.info("Henter aktive dialoginnslag for {}", aktørId);
        return tilInnslag(
                dao.findAll(where(spec(aktørId, true).and(erSpørsmål())), SORT_OPPRETTET_ASC));
    }

    private static Specification<JPAMinidialogInnslag> spec(AktørId aktørId, boolean activeOnly) {
        var spec = harAktørId(aktørId);
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
    public boolean erAlleredeLagret(String dialogId) {
        return dialogId != null && dao.findByDialogId(dialogId) != null;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [dao=" + dao + "]";
    }
}
