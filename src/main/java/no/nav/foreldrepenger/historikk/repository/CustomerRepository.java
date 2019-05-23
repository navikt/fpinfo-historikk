package no.nav.foreldrepenger.historikk.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import no.nav.foreldrepenger.historikk.domain.Customer;

public interface CustomerRepository extends CrudRepository<Customer, Long> {
    List<Customer> findByLastName(String lastName);
}
