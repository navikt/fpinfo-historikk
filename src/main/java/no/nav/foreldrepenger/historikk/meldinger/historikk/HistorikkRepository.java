package no.nav.foreldrepenger.historikk.meldinger.historikk;

import static no.nav.foreldrepenger.historikk.config.TxConfiguration.JPA_TM;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.transaction.annotation.Transactional;

import no.nav.foreldrepenger.historikk.meldinger.historikk.dao.JPAHistorikkInnslag;

@Transactional(JPA_TM)
public interface HistorikkRepository
        extends JpaRepository<JPAHistorikkInnslag, Long>, JpaSpecificationExecutor<JPAHistorikkInnslag> {

}
