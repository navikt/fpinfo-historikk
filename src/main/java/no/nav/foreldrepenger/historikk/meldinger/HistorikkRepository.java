package no.nav.foreldrepenger.historikk.meldinger;

import static no.nav.foreldrepenger.historikk.config.TxConfiguration.JPA;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import no.nav.foreldrepenger.historikk.meldinger.dto.JPAHistorikkInnslag;

@Transactional(JPA)
public interface HistorikkRepository extends JpaRepository<JPAHistorikkInnslag, Long> {

    List<JPAHistorikkInnslag> findByAktørId(String aktørId);
}
