package no.nav.foreldrepenger.historikk.tjenester.dittnav;

import static no.nav.foreldrepenger.historikk.config.JpaTxConfiguration.JPA_TM;

import no.nav.foreldrepenger.historikk.tjenester.dittnav.JPADittNavOppgave.NotifikasjonType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Transactional(JPA_TM)
public interface JPADittNavOppgaverRepository
        extends JpaRepository<JPADittNavOppgave, Long>, JpaSpecificationExecutor<JPADittNavOppgave> {

    JPADittNavOppgave findByInternReferanseIdIgnoreCase(String referanseId);

    boolean existsByInternReferanseIdIgnoreCaseAndType(String referanseId, NotifikasjonType type);

    @Query(value = """
            select o.intern_referanse_id
            from dittnavoppgaver o
            where o.ekstern_referanse_id is not null
            and o.intern_referanse_id is not null
            and o.grupperings_id is not null
            and o.sendt_done = false
            and o.opprettet < :terskel
            and o.type = 'OPPGAVE'
            group by 1
            limit :noOfRows
            """, nativeQuery = true)
    List<String> ikkeAvsluttedeOppgaver(@Param("terskel") LocalDateTime terskel, @Param("noOfRows") int noOfRows);

}
