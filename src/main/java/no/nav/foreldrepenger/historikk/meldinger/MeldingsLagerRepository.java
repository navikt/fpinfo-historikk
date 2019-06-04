package no.nav.foreldrepenger.historikk.meldinger;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import no.nav.foreldrepenger.historikk.meldinger.dto.JPAMelding;

public interface MeldingsLagerRepository extends JpaRepository<JPAMelding, Long> {
    @Query("SELECT m FROM MELDING m WHERE m.lest IS NULL AND m.aktør_id = ?1")
    List<JPAMelding> findByAktørId(String aktørId);
}
