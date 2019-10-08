package no.nav.foreldrepenger.historikk.tjenester.inntektsmelding;

import static no.nav.foreldrepenger.historikk.tjenester.inntektsmelding.JPAInntektsmelding_.aktørId;

import org.springframework.data.jpa.domain.Specification;

import no.nav.foreldrepenger.historikk.domain.AktørId;

public final class JPAInntektsmeldingSpec {

    private JPAInntektsmeldingSpec() {

    }

    public static Specification<JPAInntektsmeldingInnslag> harAktørId(AktørId id) {
        return (innslag, cq, cb) -> cb.equal(innslag.get(aktørId), id);
    }
}
