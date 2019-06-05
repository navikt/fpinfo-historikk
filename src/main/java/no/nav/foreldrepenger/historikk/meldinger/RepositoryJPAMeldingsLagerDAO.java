package no.nav.foreldrepenger.historikk.meldinger;

import java.util.List;

import org.springframework.stereotype.Repository;

import no.nav.foreldrepenger.historikk.meldinger.dto.JPAMelding;

@Repository
public class RepositoryJPAMeldingsLagerDAO implements MeldingsLagerDAO {

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
        return repo.finnUlesteMeldinger(aktørId);
    }

    @Override
    public void markerLest(String id) {
        repo.markerLest(id);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [repo=" + repo + "]";
    }

}
