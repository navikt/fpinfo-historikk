package no.nav.foreldrepenger.historikk.meldingslager;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import no.nav.foreldrepenger.historikk.meldingslager.dto.MeldingDAO;

public interface MeldingsLagerRepository extends CrudRepository<MeldingDAO, Long> {
    List<MeldingDAO> findByAktørId(String aktørId);
}
