package no.nav.foreldrepenger.historikk.tjenester.inntektsmelding;

import static no.nav.foreldrepenger.historikk.tjenester.inntektsmelding.JPAInntektsmelding_.fnr;

import org.springframework.data.jpa.domain.Specification;

import no.nav.foreldrepenger.historikk.domain.Fødselsnummer;

public final class JPAInntektsmeldingSpec {

    private JPAInntektsmeldingSpec() {

    }

    public static Specification<JPAInntektsmelding> harFnr(Fødselsnummer f) {
        return (innslag, cq, cb) -> cb.equal(innslag.get(fnr), f);
    }

}
