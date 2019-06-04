package no.nav.foreldrepenger.historikk.meldinger;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import no.nav.foreldrepenger.historikk.meldinger.dto.JPAMelding;

public interface MeldingsLagerRepository extends JpaRepository<JPAMelding, Long> {
    @Query("SELECT m FROM JPAMelding m WHERE m.aktørId = ?1 AND m.lest IS NULL")
    List<JPAMelding> finnUlesteMeldinger(String aktørId);

    @Modifying
    @Query("UPDATE JPAMelding m SET m.lest = ?2 WHERE m.aktørId = ?1")
    void merkAlle(String aktørId, LocalDate date);
}
