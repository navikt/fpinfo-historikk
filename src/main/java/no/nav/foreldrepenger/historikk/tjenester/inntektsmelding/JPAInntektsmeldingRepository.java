package no.nav.foreldrepenger.historikk.tjenester.inntektsmelding;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface JPAInntektsmeldingRepository
        extends JpaRepository<JPAInntektsmeldingInnslag, Long>, JpaSpecificationExecutor<JPAInntektsmeldingInnslag> {

    boolean existsJPAInntektsmeldingInnslagByReferanseId(String referanseId);

}
