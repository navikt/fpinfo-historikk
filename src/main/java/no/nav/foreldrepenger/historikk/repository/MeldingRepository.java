package no.nav.foreldrepenger.historikk.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import no.nav.foreldrepenger.historikk.domain.Melding;

public interface MeldingRepository extends CrudRepository<Melding, Long> {
    List<Melding> findByFnr(String fnr);
}
