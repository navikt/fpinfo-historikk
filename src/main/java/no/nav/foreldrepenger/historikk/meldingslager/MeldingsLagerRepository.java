package no.nav.foreldrepenger.historikk.meldingslager;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import no.nav.foreldrepenger.historikk.meldingslager.dto.JPAMelding;

public interface MeldingsLagerRepository extends JpaRepository<JPAMelding, Long> {
    List<JPAMelding> findByAktørId(String aktørId);
}
