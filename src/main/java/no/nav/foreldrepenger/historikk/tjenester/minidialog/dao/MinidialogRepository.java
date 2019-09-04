package no.nav.foreldrepenger.historikk.tjenester.minidialog.dao;

import static no.nav.foreldrepenger.historikk.config.TxConfiguration.JPA_TM;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import no.nav.foreldrepenger.historikk.domain.Fødselsnummer;
import no.nav.foreldrepenger.historikk.tjenester.Hendelse;

@Transactional(JPA_TM)
public interface MinidialogRepository
        extends JpaRepository<JPAMinidialogInnslag, Long>, JpaSpecificationExecutor<JPAMinidialogInnslag> {

    @Modifying
    @Query("update JPAMinidialogInnslag m set m.aktiv = false where m.fnr = :fnr and m.saksnr = :saksnr and m.hendelse = :hendelse")
    int deaktiverSak(@Param("fnr") Fødselsnummer fnr, @Param("hendelse") Hendelse hendelse,
            @Param("saksnr") String saksnr);

    @Modifying
    @Query("update JPAMinidialogInnslag m set m.aktiv = false where m.fnr = :fnr and m.hendelse = :hendelse")
    int deaktiver(@Param("fnr") Fødselsnummer fnr, @Param("hendelse") Hendelse hendelse);
}
