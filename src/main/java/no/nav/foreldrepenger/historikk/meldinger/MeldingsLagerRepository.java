package no.nav.foreldrepenger.historikk.meldinger;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import no.nav.foreldrepenger.historikk.meldinger.dto.JPAMelding;

@Transactional
public interface MeldingsLagerRepository extends JpaRepository<JPAMelding, Long> {
    @Query("select m from JPAMelding m where m.aktørId = ?1 and m.lest is null")
    List<JPAMelding> finnUlesteMeldinger(String aktørId);

    @Modifying
    @Query("update JPAMelding m set m.lest = current_date where m.id = ?1")
    void markerLest(long id);
}
