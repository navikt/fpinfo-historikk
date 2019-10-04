package no.nav.foreldrepenger.historikk.tjenester.innsending;

import static no.nav.foreldrepenger.historikk.tjenester.innsending.JPAInnsendingInnslag_.fnr;
import static no.nav.foreldrepenger.historikk.tjenester.innsending.JPAInnsendingInnslag_.opprettet;

import java.time.LocalDate;

import org.springframework.data.jpa.domain.Specification;

import no.nav.foreldrepenger.historikk.domain.Fødselsnummer;

public final class JPAInnsendingSpec {

    private JPAInnsendingSpec() {

    }

    public static Specification<JPAInnsendingInnslag> harFnr(Fødselsnummer f) {
        return (innslag, cq, cb) -> cb.equal(innslag.get(fnr), f);
    }

    public static Specification<JPAInnsendingInnslag> erEtterEllerPå(LocalDate dato) {
        return (innslag, cq, cb) -> cb.greaterThanOrEqualTo(innslag.get(opprettet), dato.atStartOfDay());
    }

}
