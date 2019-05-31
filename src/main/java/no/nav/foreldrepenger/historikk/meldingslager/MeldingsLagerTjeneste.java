package no.nav.foreldrepenger.historikk.meldingslager;

import static java.util.stream.Collectors.toList;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import no.nav.foreldrepenger.historikk.domain.AktørId;
import no.nav.foreldrepenger.historikk.domain.Melding;
import no.nav.foreldrepenger.historikk.meldingslager.dto.JPAMelding;

@Service
@Transactional
public class MeldingsLagerTjeneste {

    private final MeldingsLagerDAO dao;

    public MeldingsLagerTjeneste(MeldingsLagerDAO dao) {
        this.dao = dao;
    }

    public void lagre(Melding melding) {
        dao.lagre(new JPAMelding(melding.getAktørId().getAktørId(), melding.getMelding(), melding.getSaknr()));
    }

    @Transactional(readOnly = true)
    public List<Melding> hentMeldingerForAktør(AktørId aktørId) {
        return dao.hentForAktør(aktørId.getAktørId())
                .stream()
                .map(MeldingsLagerTjeneste::tilMelding)
                .collect(toList());
    }

    @Transactional(readOnly = true)
    public Melding hentMeldingForId(Long id) {
        return dao.hentForId(id)
                .map(MeldingsLagerTjeneste::tilMelding)
                .orElse(null);
    }

    @Transactional(readOnly = true)
    public List<Melding> hentAlle() {
        return dao.hentAlle()
                .stream()
                .map(MeldingsLagerTjeneste::tilMelding)
                .collect(toList());
    }

    private static Melding tilMelding(JPAMelding m) {
        Melding melding = new Melding(AktørId.valueOf(m.getAktørId()), m.getMelding(), m.getSaksnr());
        melding.setDato(m.getDato());
        return melding;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [dao=" + dao + "]";
    }
}
