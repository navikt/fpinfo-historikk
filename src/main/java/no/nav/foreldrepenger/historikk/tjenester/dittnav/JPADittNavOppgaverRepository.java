package no.nav.foreldrepenger.historikk.tjenester.dittnav;

import static no.nav.foreldrepenger.historikk.config.JpaTxConfiguration.JPA_TM;

import no.nav.foreldrepenger.historikk.tjenester.dittnav.JPADittNavOppgave.NotifikasjonType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.transaction.annotation.Transactional;

@Transactional(JPA_TM)
public interface JPADittNavOppgaverRepository
        extends JpaRepository<JPADittNavOppgave, Long>, JpaSpecificationExecutor<JPADittNavOppgave> {

    JPADittNavOppgave findByInternReferanseIdIgnoreCase(String referanseId);

    boolean existsByInternReferanseIdIgnoreCaseAndType(String referanseId, NotifikasjonType type);

}
