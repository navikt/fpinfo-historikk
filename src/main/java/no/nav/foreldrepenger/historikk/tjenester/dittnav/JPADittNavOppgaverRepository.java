package no.nav.foreldrepenger.historikk.tjenester.dittnav;

import static no.nav.foreldrepenger.historikk.config.TxConfiguration.JPA_TM;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.transaction.annotation.Transactional;

@Transactional(JPA_TM)
public interface JPADittNavOppgaverRepository
        extends JpaRepository<JPADittNavOppgave, Long>, JpaSpecificationExecutor<JPADittNavOppgave> {

    JPADittNavOppgave findByReferanseIdIgnoreCase(String referanseId);

    boolean existsByReferanseIdIgnoreCase(String referanseId);

}
