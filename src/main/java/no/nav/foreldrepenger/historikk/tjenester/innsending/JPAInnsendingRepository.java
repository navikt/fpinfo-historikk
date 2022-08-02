package no.nav.foreldrepenger.historikk.tjenester.innsending;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import no.nav.foreldrepenger.historikk.domain.Fødselsnummer;

public interface JPAInnsendingRepository
        extends JpaRepository<JPAInnsendingInnslag, Long>, JpaSpecificationExecutor<JPAInnsendingInnslag> {
    JPAInnsendingInnslag findByReferanseId(String referanseId);

    List<JPAInnsendingInnslag> findBySaksnrAndFnrOrderByOpprettetAsc(String saksnr, Fødselsnummer fnr);

}
