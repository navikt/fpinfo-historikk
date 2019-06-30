package no.nav.foreldrepenger.historikk.tjenester.minidialog.dao;

import static no.nav.foreldrepenger.historikk.config.TxConfiguration.JPA_TM;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import no.nav.foreldrepenger.historikk.tjenester.innsending.Hendelse;

@Transactional(JPA_TM)
public interface MinidialogRepository
        extends JpaRepository<JPAMinidialogInnslag, Long>, JpaSpecificationExecutor<JPAMinidialogInnslag> {

    @Modifying
    @Query("update JPAMinidialogInnslag m set m.aktiv = false where m.aktørId = :aktørId and m.saksnr = :saksnr and m.handling = :handling")
    int deaktiverSak(@Param("aktørId") String aktørId, @Param("handling") Hendelse handling,
            @Param("saksnr") String saksnr);

    @Modifying
    @Query("update JPAMinidialogInnslag m set m.aktiv = false where m.aktørId = :aktørId and m.handling = :handling")
    int deaktiver(@Param("aktørId") String aktørId, @Param("handling") Hendelse handling);
}
