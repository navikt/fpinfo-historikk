package no.nav.foreldrepenger.historikk.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import no.nav.foreldrepenger.historikk.domain.Customer1;

public interface CustomerRepository extends CrudRepository<Customer1, Long> {
    List<Customer1> findByLastName(String lastName);
}
