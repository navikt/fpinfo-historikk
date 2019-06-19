package no.nav.foreldrepenger.historikk.meldinger;

import static java.util.stream.Collectors.toList;
import static no.nav.foreldrepenger.historikk.config.TxConfiguration.JPA;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import no.nav.foreldrepenger.historikk.domain.AktørId;
import no.nav.foreldrepenger.historikk.domain.LeveranseKanal;
import no.nav.foreldrepenger.historikk.domain.MinidialogInnslag;
import no.nav.foreldrepenger.historikk.meldinger.dto.JPAMinidialogInnslag;
import no.nav.foreldrepenger.historikk.meldinger.event.SøknadType;

@Service
@Transactional(JPA)
public class MinidialogTjeneste {

    private final MinidialogRepository dao;
    private final OppslagTjeneste oppslag;

    public MinidialogTjeneste(MinidialogRepository dao, OppslagTjeneste oppslag) {
        this.dao = dao;
        this.oppslag = oppslag;
    }

    public int deaktiverMineMinidaloger(SøknadType type) {
        return deaktiver(oppslag.hentAktørId(), type);
    }

    public int deaktiverMinidialoger(AktørId aktørId, SøknadType type) {
        return deaktiver(aktørId, type);
    }

    private int deaktiver(AktørId aktørId, SøknadType type) {
        return dao.deaktiver(aktørId.getAktørId(), type.name());
    }

    public void lagre(MinidialogInnslag m) {
        dao.save(fraInnslag(m));
    }

    @Transactional(readOnly = true)
    public List<MinidialogInnslag> hentAktiveDialogerForAktør(AktørId aktørId) {
        return dao.finnAktiveDialoger(aktørId.getAktørId())
                .stream()
                .map(MinidialogTjeneste::tilInnslag)
                .collect(toList());
    }

    @Transactional(readOnly = true)
    public List<MinidialogInnslag> hentMineAktiveDialoger() {
        return hentAktiveDialogerForAktør(oppslag.hentAktørId());
    }

    private static JPAMinidialogInnslag fraInnslag(MinidialogInnslag m) {
        JPAMinidialogInnslag dialog = new JPAMinidialogInnslag(m.getAktørId(), m.getMelding(),
                m.getSaksnr(),
                m.getKanal().name());
        dialog.setGyldigTil(m.getGyldigTil());
        dialog.setHandling(m.getHandling().name());
        dialog.setAktiv(m.isAktiv());
        return dialog;
    }

    private static MinidialogInnslag tilInnslag(JPAMinidialogInnslag m) {
        MinidialogInnslag melding = new MinidialogInnslag(m.getAktørId(), m.getMelding(),
                m.getSaksnr());
        melding.setEndret(m.getEndret());
        melding.setOpprettet(m.getOpprettet());
        melding.setKanal(LeveranseKanal.valueOf(m.getKanal()));
        melding.setId(m.getId());
        melding.setGyldigTil(m.getGyldigTil());
        if (m.getHandling() != null) {
            melding.setHandling(SøknadType.valueOf(m.getHandling()));
        }
        melding.setAktiv(m.isAktiv());
        return melding;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [dao=" + dao + ", oppslag=" + oppslag + "]";
    }

}
