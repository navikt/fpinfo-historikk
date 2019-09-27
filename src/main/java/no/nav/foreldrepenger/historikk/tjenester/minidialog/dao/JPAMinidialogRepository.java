package no.nav.foreldrepenger.historikk.tjenester.minidialog.dao;

import static no.nav.foreldrepenger.historikk.config.TxConfiguration.JPA_TM;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import no.nav.foreldrepenger.historikk.domain.Fødselsnummer;

@Transactional(JPA_TM)
public interface JPAMinidialogRepository
        extends JpaRepository<JPAMinidialogInnslag, Long>, JpaSpecificationExecutor<JPAMinidialogInnslag> {

    JPAMinidialogInnslag findByReferanseId(String referanseId);

    @Modifying
    @Query("update JPAMinidialogInnslag m set m.aktiv = false where m.fnr = :fnr and m.referanseId = :referanseId")
    int deaktiver(@Param("fnr") Fødselsnummer fnr, @Param("referanseId") String referanseId);

}
