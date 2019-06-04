package no.nav.foreldrepenger.historikk.meldinger;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import no.nav.foreldrepenger.historikk.meldinger.dto.JPAMelding;

public interface MeldingsLagerRepository extends JpaRepository<JPAMelding, Long> {
    @Query("SELECT m FROM JPAMelding m WHERE m.aktørId = ?1 AND m.lest IS NULL")
    List<JPAMelding> finnUlesteMeldinger(String aktørId);

    @Modifying
    @Query(value = "UPDATE melding m set m.lest = CURRENT_DATE where m.aktør_id = ?1", nativeQuery = true)
    void merkAlle(String aktørId);
}
