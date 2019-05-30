package no.nav.foreldrepenger.historikk.meldingslager;

import java.util.List;

import org.springframework.stereotype.Repository;

import no.nav.foreldrepenger.historikk.meldingslager.dto.MeldingDAO;

@Repository
public class JPAMeldingsLagerDAO implements MeldingslagerDAO {

    private final MeldingsLagerRepository repository;

    public JPAMeldingsLagerDAO(MeldingsLagerRepository repository) {
        this.repository = repository;
    }

    @Override
    public void lagre(MeldingDAO meldingDAO) {
        repository.save(meldingDAO);
    }

    @Override
    public List<MeldingDAO> hent(String aktørId) {
        return repository.findByAktørId(aktørId);
    }
}
