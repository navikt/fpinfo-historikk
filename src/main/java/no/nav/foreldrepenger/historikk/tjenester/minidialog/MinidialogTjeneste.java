package no.nav.foreldrepenger.historikk.tjenester.minidialog;

import static java.util.stream.Collectors.toList;
import static no.nav.foreldrepenger.historikk.config.TxConfiguration.JPA_TM;
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
import no.nav.foreldrepenger.historikk.tjenester.felles.Hendelse;
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

    public void deaktiver(Hendelse hendelse) {
        if (hendelse.erEttersending() && hendelse.getReferanseId() != null) {
            int n = dao.deaktiver(hendelse.getAktørId(), hendelse.getReferanseId());
            LOG.info("Deaktiverte {} minidialog{} for referanseId {} etter hendelse {}", n, flertall(n),
                    hendelse.getReferanseId(),
                    hendelse.getHendelseType());
        } else {
            LOG.info("Ingen deaktivering for {} og hendelse {}", hendelse.getAktørId(), hendelse.getHendelseType());
        }
    }

    @Override
    public void lagre(MinidialogHendelse hendelse) {
        if (!erAlleredeLagret(hendelse.getReferanseId())) {
            LOG.info("Lagrer minidialog {}", hendelse);
            dao.save(fraHendelse(hendelse));
            LOG.info("Lagret minidialog OK");
            deaktiver(hendelse);
        } else {
            LOG.info("Hendelse med referanseId {} er allerede lagret", hendelse.getReferanseId());
        }
    }

    @Transactional(readOnly = true)
    public List<MinidialogInnslag> dialoger(boolean activeOnly) {
        return dialoger(oppslag.aktørId(), activeOnly);
    }

    @Transactional(readOnly = true)
    public List<MinidialogInnslag> dialoger(AktørId id, boolean activeOnly) {
        LOG.info("Henter dialoger for {} og activeOnly={}", id, activeOnly);
        return tilInnslag(dao.findAll(where(spec(id, activeOnly)), SORT_OPPRETTET_ASC));
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

    private static Specification<JPAMinidialogInnslag> spec(AktørId id, boolean activeOnly) {
        var spec = harAktørId(id);
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
        return getClass().getSimpleName() + " [dao=" + dao + "]";
    }
}
