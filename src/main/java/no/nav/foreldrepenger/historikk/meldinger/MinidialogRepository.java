package no.nav.foreldrepenger.historikk.meldinger;

import static no.nav.foreldrepenger.historikk.config.TxConfiguration.JPA;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import no.nav.foreldrepenger.historikk.meldinger.dto.JPAMinidialogInnslag;

@Transactional(JPA)
public interface MinidialogRepository extends JpaRepository<JPAMinidialogInnslag, Long> {

    List<JPAMinidialogInnslag> findByAktørId(String aktørId);

    List<JPAMinidialogInnslag> findByAktørIdAndAktivIsTrue(String aktørId);

    @Modifying
    @Query("update JPAMinidialogInnslag m set m.aktiv = false where m.aktørId = :aktørId and m.handling = :handling")
    int deaktiver(@Param("aktørId") String aktørId, @Param("handling") String handling);
}
