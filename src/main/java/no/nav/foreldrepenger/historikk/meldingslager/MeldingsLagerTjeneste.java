package no.nav.foreldrepenger.historikk.meldingslager;

import static java.util.stream.Collectors.toList;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import no.nav.foreldrepenger.historikk.domain.AktørId;
import no.nav.foreldrepenger.historikk.domain.Melding;
import no.nav.foreldrepenger.historikk.meldingslager.dto.MeldingDAO;

@Service
@Transactional
public class MeldingsLagerTjeneste {
    private final MeldingslagerDAO dao;

    public MeldingsLagerTjeneste(MeldingslagerDAO dao) {
        this.dao = dao;
    }

    public void lagre(Melding melding) {
        dao.lagre(new MeldingDAO(melding.getAktørId().getAktørId(), melding.getMelding()));
    }

    public List<Melding> hentMeldinger(AktørId aktørId) {
        return dao.hent(aktørId.getAktørId())
                .stream()
                .map(MeldingsLagerTjeneste::tilMelding)
                .collect(toList());
    }

    private static Melding tilMelding(MeldingDAO dao) {
        return new Melding(dao.getId(), AktørId.valueOf(dao.getAktørId()), dao.getMelding());
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [dao=" + dao + "]";
    }
}
