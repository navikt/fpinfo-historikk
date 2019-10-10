package no.nav.foreldrepenger.historikk.tjenester.minidialog;

import static no.nav.foreldrepenger.historikk.config.TxConfiguration.JPA_TM;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import no.nav.foreldrepenger.historikk.domain.AktørId;

@Transactional(JPA_TM)
public interface JPAMinidialogRepository
        extends JpaRepository<JPAMinidialogInnslag, Long>, JpaSpecificationExecutor<JPAMinidialogInnslag> {

    JPAMinidialogInnslag findByReferanseId(String referanseId);

    @Modifying
    @Query("update JPAMinidialogInnslag m set m.aktiv = false where m.aktørId = :aktørId and m.dialogId = :dialogId and m.dialogId is not null")
    int deaktiver(@Param("aktørId") AktørId aktørId, @Param("dialogId") String dialogId);

}
