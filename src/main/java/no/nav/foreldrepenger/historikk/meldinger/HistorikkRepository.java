package no.nav.foreldrepenger.historikk.meldinger;

import static no.nav.foreldrepenger.historikk.config.TxConfiguration.JPA;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.transaction.annotation.Transactional;

import no.nav.foreldrepenger.historikk.meldinger.dao.JPAHistorikkInnslag;

@Transactional(JPA)
public interface HistorikkRepository
        extends JpaRepository<JPAHistorikkInnslag, Long>, JpaSpecificationExecutor<JPAHistorikkInnslag> {

}
