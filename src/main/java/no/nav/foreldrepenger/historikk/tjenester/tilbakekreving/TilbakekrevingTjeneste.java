package no.nav.foreldrepenger.historikk.tjenester.tilbakekreving;

import static java.util.stream.Collectors.toList;
import static no.nav.foreldrepenger.historikk.config.TxConfiguration.JPA_TM;
import static no.nav.foreldrepenger.historikk.tjenester.felles.HendelseType.TILBAKEKREVING_SVAR;
import static no.nav.foreldrepenger.historikk.tjenester.felles.HistorikkInnslag.SORT_OPPRETTET_ASC;
import static no.nav.foreldrepenger.historikk.tjenester.tilbakekreving.JPATilbakekrevingSpec.erAktiv;
import static no.nav.foreldrepenger.historikk.tjenester.tilbakekreving.JPATilbakekrevingSpec.erGyldig;
import static no.nav.foreldrepenger.historikk.tjenester.tilbakekreving.JPATilbakekrevingSpec.erSpørsmål;
import static no.nav.foreldrepenger.historikk.tjenester.tilbakekreving.JPATilbakekrevingSpec.gyldigErNull;
import static no.nav.foreldrepenger.historikk.tjenester.tilbakekreving.JPATilbakekrevingSpec.harAktørId;
import static no.nav.foreldrepenger.historikk.tjenester.tilbakekreving.TilbakekrevingMapper.fraHendelse;
import static no.nav.foreldrepenger.historikk.util.StreamUtil.safeStream;
import static no.nav.foreldrepenger.historikk.util.StringUtil.flertall;
import static org.springframework.data.jpa.domain.Specification.where;

import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import no.nav.foreldrepenger.boot.conditionals.EnvUtil;
import no.nav.foreldrepenger.historikk.domain.AktørId;
import no.nav.foreldrepenger.historikk.domain.Fødselsnummer;
import no.nav.foreldrepenger.historikk.tjenester.felles.IdempotentTjeneste;
import no.nav.foreldrepenger.historikk.tjenester.oppslag.Oppslag;

@Service
@Transactional(JPA_TM)
public class TilbakekrevingTjeneste implements IdempotentTjeneste<TilbakekrevingHendelse>, EnvironmentAware {

    private static final Logger LOG = LoggerFactory.getLogger(TilbakekrevingTjeneste.class);

    private final JPATilbakekrevingRepository dao;
    private final Oppslag oppslag;

    private Environment env;

    public TilbakekrevingTjeneste(JPATilbakekrevingRepository dao, Oppslag oppslag) {
        this.dao = dao;
        this.oppslag = oppslag;
    }

    public void avsluttOppgave(Fødselsnummer fnr, String dialogId) {
        int n = dao.deaktiver(fnr, dialogId);
        LOG.info("Deaktiverte {} minidialog{} for fnr {} og  dialogId {} etter hendelse", n, flertall(n),
                fnr, dialogId);
    }

    public void deaktiver(AktørId aktørId, String dialogId) {
        int n = dao.deaktiver(aktørId, dialogId);
        LOG.info("Deaktiverte {} minidialog{} for aktør {} og  dialogId {} etter hendelse", n, flertall(n),
                aktørId, dialogId);
    }

    @Override
    public boolean opprettOppgave(TilbakekrevingHendelse h) {
        var orig = dao.findByDialogId(h.getDialogId());
        if (orig == null) {
            LOG.info("Lagrer minidialog {}", h);
            dao.save(fraHendelse(h));
            LOG.info("Lagret minidialog OK");
            if (TILBAKEKREVING_SVAR.equals(h.getHendelse())) {
                avsluttOppgave(h.getFnr(), h.getDialogId());
            }
            return true;
        }
        LOG.info("Hendelse med dialogId {} er allerede lagret, oppdaterer felter", h.getDialogId());
        orig.setAktiv(h.isAktiv());
        orig.setGyldigTil(h.getGyldigTil());
        dao.save(orig);
        return false;
    }

    @Transactional(readOnly = true)
    public List<TilbakekrevingInnslag> dialoger(boolean activeOnly) {
        return dialoger(oppslag.aktørId(), activeOnly);
    }

    @Transactional(readOnly = true)
    public List<TilbakekrevingInnslag> dialoger(AktørId aktørId, boolean activeOnly) {
        LOG.info("Henter dialoginnslag for {} og activeOnly={}", aktørId, activeOnly);
        return tilInnslag(dao.findAll(where(spec(aktørId, activeOnly)), SORT_OPPRETTET_ASC));
    }

    @Transactional(readOnly = true)
    public List<TilbakekrevingInnslag> aktive() {
        return aktive(oppslag.aktørId());
    }

    @Transactional(readOnly = true)
    public List<TilbakekrevingInnslag> aktive(AktørId aktørId) {
        LOG.info("Henter aktive dialoginnslag for {}", aktørId);
        if (EnvUtil.isDevOrLocal(env)) {
            return tilInnslag(
                    dao.findAll(where(spec(aktørId, true).and(erSpørsmål())), SORT_OPPRETTET_ASC));
        }
        LOG.info("Returnerer ikke minidialoginnslag i prod foreløpig");
        return Collections.emptyList(); // ikke i prod foreløpig
    }

    private static Specification<JPATilbakekrevingInnslag> spec(AktørId aktørId, boolean activeOnly) {
        var spec = harAktørId(aktørId);
        var retur = activeOnly ? spec
                .and((erGyldig().or(gyldigErNull())))
                .and(erAktiv()) : spec;

        return retur;
    }

    private static List<TilbakekrevingInnslag> tilInnslag(List<JPATilbakekrevingInnslag> innslag) {
        var i = safeStream(innslag)
                .map(TilbakekrevingMapper::tilInnslag)
                .collect(toList());
        LOG.info("Hentet {} dialog{} ({})", i.size(), flertall(i), i);
        return i;
    }

    @Override
    public void setEnvironment(Environment env) {
        this.env = env;

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
