package no.nav.foreldrepenger.historikk.meldinger;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import no.nav.foreldrepenger.historikk.meldinger.dto.JPAMelding;

public interface MeldingsLagerRepository extends JpaRepository<JPAMelding, Long> {
    @Query("SELECT m FROM Melding m WHERE m.lest IS NULL")
    List<JPAMelding> findByAktørId(String aktørId);
}
