package no.nav.foreldrepenger.historikk.meldinger;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import no.nav.foreldrepenger.historikk.meldinger.dto.JPAMelding;

@Repository
public class JPAMeldingsLagerDAO implements MeldingsLagerDAO {

    private final MeldingsLagerRepository repository;

    public JPAMeldingsLagerDAO(MeldingsLagerRepository repository) {
        this.repository = repository;
    }

    @Override
    public void lagre(JPAMelding meldingDAO) {
        repository.save(meldingDAO);
    }

    @Override
    public List<JPAMelding> hentForAktør(String aktørId) {
        return repository.finnUlesteMeldinger(aktørId);
    }

    @Override
    public Optional<JPAMelding> hentForId(Long id) {
        return repository.findById(id);
    }

    @Override
    public List<JPAMelding> hentAlle() {
        return repository.findAll();
    }

    @Override
    public void merkAlle(String aktørId) {
        repository.merkAlle(aktørId);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [repository=" + repository + "]";
    }

}
