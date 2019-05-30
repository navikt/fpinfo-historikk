package no.nav.foreldrepenger.historikk.meldingslager;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import no.nav.foreldrepenger.historikk.meldingslager.dto.JPAMeldingDAO;

public interface MeldingsLagerRepository extends CrudRepository<JPAMeldingDAO, Long> {
    List<JPAMeldingDAO> findByAktørId(String aktørId);
}
