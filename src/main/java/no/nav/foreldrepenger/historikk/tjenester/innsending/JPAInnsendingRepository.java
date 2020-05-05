package no.nav.foreldrepenger.historikk.tjenester.innsending;

import static no.nav.foreldrepenger.historikk.config.TxConfiguration.JPA_TM;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.transaction.annotation.Transactional;

@Transactional(JPA_TM)
public interface JPAInnsendingRepository
        extends JpaRepository<JPAInnsendingInnslag, Long>, JpaSpecificationExecutor<JPAInnsendingInnslag> {
    JPAInnsendingInnslag findByReferanseId(String referanseId);

    List<JPAInnsendingInnslag> findBySaksnrOrderByOpprettetAsc(String saksnr);

}
