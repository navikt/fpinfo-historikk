package no.nav.foreldrepenger.historikk.meldinger;

import static no.nav.foreldrepenger.historikk.config.TxConfiguration.JPA;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import no.nav.foreldrepenger.historikk.meldinger.dto.JPAMinidialogInnslag;

@Transactional(JPA)
public interface MeldingsLagerRepository extends JpaRepository<JPAMinidialogInnslag, Long> {

    @Modifying
    @Query("update JPAMinidialogInnslag m set m.lest = current_date where m.id = ?1")
    void markerLest(long id);

    List<JPAMinidialogInnslag> findByAktørId(String aktørId);
}
