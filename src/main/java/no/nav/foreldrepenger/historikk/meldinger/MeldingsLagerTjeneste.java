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
public class MeldingsLagerTjeneste {

    private static final Logger LOG = LoggerFactory.getLogger(MeldingsLagerTjeneste.class);

    private final MeldingsLagerDAO dao;
    private final OppslagConnection oppslag;

    public MeldingsLagerTjeneste(MeldingsLagerDAO dao, OppslagConnection oppslag) {
        this.dao = dao;
        this.oppslag = oppslag;
    }

    public void lagre(MinidialogInnslag m) {
        dao.lagre(fraMelding(m));
    }

    @Transactional(readOnly = true)
    public List<MinidialogInnslag> hentMeldingerForAktør(AktørId aktørId) {
        return dao.hentForAktør(aktørId.getAktørId())
                .stream()
                .map(MeldingsLagerTjeneste::tilMelding)
                .collect(toList());
    }

    @Transactional(readOnly = true)
    public List<MinidialogInnslag> hentMineMeldinger() {
        return hentMeldingerForAktør(oppslag.hentAktørId());
    }

    public void markerLest(long id) {
        dao.markerLest(id, oppslag.hentAktørId());
    }

    private static JPAMinidialogInnslag fraMelding(MinidialogInnslag m) {
        return new JPAMinidialogInnslag(m.getAktørId().getAktørId(), m.getMelding(), m.getSaknr(), m.getKanal().name());
    }

    private static MinidialogInnslag tilMelding(JPAMinidialogInnslag m) {
        MinidialogInnslag melding = new MinidialogInnslag(AktørId.valueOf(m.getAktørId()), m.getMelding(), m.getSaksnr());
        melding.setDato(m.getDato());
        melding.setKanal(LeveranseKanal.valueOf(m.getKanal()));
        melding.setLest(m.getLest());
        melding.setId(m.getId());
        return melding;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [dao=" + dao + ", oppslag=" + oppslag + "]";
    }

}
