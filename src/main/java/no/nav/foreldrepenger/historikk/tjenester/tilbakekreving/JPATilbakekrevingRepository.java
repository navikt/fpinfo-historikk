package no.nav.foreldrepenger.historikk.tjenester.tilbakekreving;

import static no.nav.foreldrepenger.historikk.config.JpaTxConfiguration.JPA_TM;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import no.nav.foreldrepenger.historikk.domain.AktørId;
import no.nav.foreldrepenger.historikk.domain.Fødselsnummer;

@Transactional(JPA_TM)
public interface JPATilbakekrevingRepository
        extends JpaRepository<JPATilbakekrevingInnslag, Long>, JpaSpecificationExecutor<JPATilbakekrevingInnslag> {

    JPATilbakekrevingInnslag findByDialogId(String dialogId);

    @Modifying
    @Query("update JPATilbakekrevingInnslag m set m.aktiv = false where m.fnr = :fnr and m.dialogId = :dialogId and m.dialogId is not null")
    int deaktiver(@Param("fnr") Fødselsnummer fnr, @Param("dialogId") String dialogId);

    @Modifying
    @Query("update JPATilbakekrevingInnslag m set m.aktiv = false where m.aktørId = :aktørId and m.dialogId = :dialogId and m.dialogId is not null")
    int deaktiver(@Param("aktørId") AktørId aktørId, @Param("dialogId") String dialogId);

}
