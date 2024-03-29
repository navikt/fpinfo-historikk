package no.nav.foreldrepenger.historikk.tjenester.tilbakekreving;

import static no.nav.foreldrepenger.common.util.StreamUtil.safeStream;
import static no.nav.foreldrepenger.common.util.StringUtil.flertall;
import static no.nav.foreldrepenger.historikk.tjenester.felles.HistorikkInnslag.SORT_OPPRETTET_ASC;
import static no.nav.foreldrepenger.historikk.tjenester.tilbakekreving.JPATilbakekrevingSpec.erSpørsmål;
import static no.nav.foreldrepenger.historikk.tjenester.tilbakekreving.JPATilbakekrevingSpec.spec;
import static no.nav.foreldrepenger.historikk.tjenester.tilbakekreving.TilbakekrevingMapper.fraHendelse;
import static org.springframework.data.jpa.domain.Specification.where;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import no.nav.foreldrepenger.historikk.domain.AktørId;
import no.nav.foreldrepenger.historikk.domain.Fødselsnummer;
import no.nav.foreldrepenger.historikk.tjenester.oppslag.Oppslag;

@Service
public class Tilbakekreving {

    private static final Logger LOG = LoggerFactory.getLogger(Tilbakekreving.class);

    private final JPATilbakekrevingRepository dao;
    private final Oppslag oppslag;

    public Tilbakekreving(JPATilbakekrevingRepository dao, Oppslag oppslag) {
        this.dao = dao;
        this.oppslag = oppslag;
    }

    public void avsluttOppgave(TilbakekrevingHendelse h) {
        avsluttOppgave(h.getFnr(), h.getDialogId());
        dao.save(fraHendelse(h));
    }

    private void avsluttOppgave(Fødselsnummer fnr, String dialogId) {
        int n = dao.deaktiver(fnr, dialogId);
        LOG.debug("Deaktiverte {} tilbakekreving{} for dialogId {}", n, flertall(n), dialogId);

    }

    public void avsluttOppgave(AktørId aktørId, String dialogId) {
        int n = dao.deaktiver(aktørId, dialogId);
        LOG.debug("Deaktiverte {} tilbakekrevingsdialoger{} for dialogId {}", n, flertall(n), dialogId);
    }

    public void opprettOppgave(TilbakekrevingHendelse h) {
        if (h.getFnr() != null) {
            avsluttOppgave(h.getFnr(), h.getDialogId());
        } else {
            avsluttOppgave(h.getAktørId(), h.getDialogId());
        }
        LOG.info("Lagrer tilbakekrevingsdialog {}", h);
        dao.save(fraHendelse(h));
        LOG.info("Lagret tilbakekrevingsdialog OK");
    }

    @Transactional(readOnly = true)
    public List<TilbakekrevingInnslag> tilbakekrevinger(AktørId aktørId, boolean activeOnly) {
        LOG.info("Henter tilbakekrevingsdialoger hvor activeOnly={}", activeOnly);
        return tilInnslag(dao.findAll(where(spec(aktørId, activeOnly)), SORT_OPPRETTET_ASC));
    }

    @Transactional(readOnly = true)
    public List<TilbakekrevingInnslag> aktive(AktørId aktørId) {
        LOG.info("Henter aktive tilbakekrevingsdialoger");
        return tilInnslag(dao.findAll(where(spec(aktørId, true).and(erSpørsmål())), SORT_OPPRETTET_ASC));
    }

    private static List<TilbakekrevingInnslag> tilInnslag(List<JPATilbakekrevingInnslag> innslag) {
        var spm = safeStream(innslag)
                .map(TilbakekrevingMapper::tilInnslag)
                .toList();
        LOG.info("Hentet {} tilbakekrevingsdialog{}", spm.size(), flertall(spm));
        return spm;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [dao=" + dao + "]";
    }

}
