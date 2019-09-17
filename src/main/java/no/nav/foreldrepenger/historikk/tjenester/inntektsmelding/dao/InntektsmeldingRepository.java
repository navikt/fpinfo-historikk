package no.nav.foreldrepenger.historikk.tjenester.inntektsmelding.dao;

import static no.nav.foreldrepenger.historikk.config.TxConfiguration.JPA_TM;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.transaction.annotation.Transactional;

@Transactional(JPA_TM)
public interface InntektsmeldingRepository
        extends JpaRepository<JPAInntektsmelding, Long>, JpaSpecificationExecutor<JPAInntektsmelding> {

}
