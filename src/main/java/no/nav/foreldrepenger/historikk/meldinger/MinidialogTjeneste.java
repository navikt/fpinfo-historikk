package no.nav.foreldrepenger.historikk.meldinger;

import static java.util.stream.Collectors.toList;
import static no.nav.foreldrepenger.historikk.config.TxConfiguration.JPA;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import no.nav.foreldrepenger.historikk.domain.AktørId;
import no.nav.foreldrepenger.historikk.domain.LeveranseKanal;
import no.nav.foreldrepenger.historikk.domain.MinidialogInnslag;
import no.nav.foreldrepenger.historikk.meldinger.dto.JPAMinidialogInnslag;

@Service
@Transactional(JPA)
public class MinidialogTjeneste {

    private static final Logger LOG = LoggerFactory.getLogger(MinidialogTjeneste.class);

    private final RepositoryJPAMeldingsLagerDAO dao;
    private final OppslagConnection oppslag;

    public MinidialogTjeneste(RepositoryJPAMeldingsLagerDAO dao, OppslagConnection oppslag) {
        this.dao = dao;
        this.oppslag = oppslag;
    }

    public void lagre(MinidialogInnslag m) {
        dao.lagre(fraInnslag(m));
    }

    @Transactional(readOnly = true)
    public List<MinidialogInnslag> hentDialogerForAktør(AktørId aktørId) {
        return dao.hentForAktør(aktørId.getAktørId())
                .stream()
                .map(MinidialogTjeneste::tilInnslag)
                .collect(toList());
    }

    @Transactional(readOnly = true)
    public List<MinidialogInnslag> hentMineDialoger() {
        return hentDialogerForAktør(oppslag.hentAktørId());
    }

    private static JPAMinidialogInnslag fraInnslag(MinidialogInnslag m) {
        JPAMinidialogInnslag dialog = new JPAMinidialogInnslag(m.getAktørId().getAktørId(), m.getMelding(),
                m.getSaknr(),
                m.getKanal().name());
        dialog.setGyldigTil(m.getGyldigTil());
        return dialog;
    }

    private static MinidialogInnslag tilInnslag(JPAMinidialogInnslag m) {
        MinidialogInnslag melding = new MinidialogInnslag(AktørId.valueOf(m.getAktørId()), m.getMelding(),
                m.getSaksnr());
        melding.setDato(m.getDato());
        melding.setKanal(LeveranseKanal.valueOf(m.getKanal()));
        melding.setId(m.getId());
        melding.setGyldigTil(m.getGyldigTil());
        return melding;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [dao=" + dao + ", oppslag=" + oppslag + "]";
    }

}
