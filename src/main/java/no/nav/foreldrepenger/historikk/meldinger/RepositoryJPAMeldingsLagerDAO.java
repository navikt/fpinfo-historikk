package no.nav.foreldrepenger.historikk.meldinger;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import no.nav.foreldrepenger.historikk.domain.AktørId;
import no.nav.foreldrepenger.historikk.meldinger.dto.JPAMelding;

@Repository
public class RepositoryJPAMeldingsLagerDAO implements MeldingsLagerDAO {

    private static final Logger LOG = LoggerFactory.getLogger(RepositoryJPAMeldingsLagerDAO.class);

    private final MeldingsLagerRepository repo;

    public RepositoryJPAMeldingsLagerDAO(MeldingsLagerRepository repo) {
        this.repo = repo;
    }

    @Override
    public void lagre(JPAMelding meldingDAO) {
        repo.save(meldingDAO);
    }

    @Override
    public List<JPAMelding> hentForAktør(String aktørId) {
        return repo.findByAktørId(aktørId);
    }

    @Override
    public void markerLest(long id, AktørId aktørId) {
        repo.markerLest(id);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [repo=" + repo + "]";
    }
}
