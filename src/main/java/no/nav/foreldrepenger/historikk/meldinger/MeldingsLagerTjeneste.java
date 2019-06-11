package no.nav.foreldrepenger.historikk.meldinger;

import static java.util.stream.Collectors.toList;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import no.nav.foreldrepenger.historikk.domain.AktørId;
import no.nav.foreldrepenger.historikk.domain.LeveranseKanal;
import no.nav.foreldrepenger.historikk.domain.Melding;
import no.nav.foreldrepenger.historikk.meldinger.dto.JPAMelding;

@Service
@Transactional("jpa")
public class MeldingsLagerTjeneste {

    private static final Logger LOG = LoggerFactory.getLogger(MeldingsLagerTjeneste.class);

    private final MeldingsLagerDAO dao;
    private final OppslagConnection oppslag;

    public MeldingsLagerTjeneste(MeldingsLagerDAO dao, OppslagConnection oppslag) {
        this.dao = dao;
        this.oppslag = oppslag;
    }

    public void lagre(Melding m) {
        dao.lagre(fraMelding(m));
    }

    @Transactional(readOnly = true)
    public List<Melding> hentMeldingerForAktør(AktørId aktørId) {
        return dao.hentForAktør(aktørId.getAktørId())
                .stream()
                .map(MeldingsLagerTjeneste::tilMelding)
                .collect(toList());
    }

    @Transactional(readOnly = true)
    public List<Melding> hentMineMeldinger() {
        return hentMeldingerForAktør(oppslag.hentAktørId());
    }

    public void markerLest(long id) {
        dao.markerLest(id, oppslag.hentAktørId());
    }

    private static JPAMelding fraMelding(Melding m) {
        return new JPAMelding(m.getAktørId().getAktørId(), m.getMelding(), m.getSaknr(), m.getKanal().name());
    }

    private static Melding tilMelding(JPAMelding m) {
        Melding melding = new Melding(AktørId.valueOf(m.getAktørId()), m.getMelding(), m.getSaksnr());
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
