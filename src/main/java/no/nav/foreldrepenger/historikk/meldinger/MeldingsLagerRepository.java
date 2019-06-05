package no.nav.foreldrepenger.historikk.meldinger;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import no.nav.foreldrepenger.historikk.meldinger.dto.JPAMelding;

@Transactional
public interface MeldingsLagerRepository extends JpaRepository<JPAMelding, Long> {
    @Query("SELECT m FROM JPAMelding m WHERE m.aktørId = ?1 AND m.lest IS NULL")
    List<JPAMelding> finnUlesteMeldinger(String aktørId);

    @Modifying
    @Query("update JPAMelding m set  m.lest = NULL where m.aktørId = ?1")
    void merkAlle(String aktørid);
}
