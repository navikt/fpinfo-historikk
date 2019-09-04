package no.nav.foreldrepenger.historikk.tjenester.søknad.dao;

import static no.nav.foreldrepenger.historikk.config.TxConfiguration.JPA_TM;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.transaction.annotation.Transactional;

@Transactional(JPA_TM)
public interface SøknadsHistorikkRepository
        extends JpaRepository<JPASøknadsHistorikkInnslag, Long>, JpaSpecificationExecutor<JPASøknadsHistorikkInnslag> {

}
