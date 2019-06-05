package no.nav.foreldrepenger.historikk.meldinger;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import no.nav.foreldrepenger.historikk.meldinger.dto.JPAMelding;

public interface MeldingsLagerRepository extends JpaRepository<JPAMelding, Long> {
    @Query("SELECT m FROM JPAMelding m WHERE m.aktørId = ?1 AND m.lest IS NULL")
    List<JPAMelding> finnUlesteMeldinger(String aktørId);

    @Modifying
    @Query("UPDATE JPAMelding m SET m.lest = NULL  WHERE m.aktørId = :aktørid")
    void merkAlle(@Param("aktørid") String aktørid);
}
