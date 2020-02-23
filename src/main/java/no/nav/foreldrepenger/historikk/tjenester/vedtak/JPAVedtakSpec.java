package no.nav.foreldrepenger.historikk.tjenester.vedtak;

import static java.time.LocalDate.now;
import static no.nav.foreldrepenger.historikk.tjenester.vedtak.JPAVedtakInnslag_.fnr;
import static no.nav.foreldrepenger.historikk.tjenester.vedtak.JPAVedtakInnslag_.gyldigTil;

import org.springframework.data.jpa.domain.Specification;

import no.nav.foreldrepenger.historikk.domain.Fødselsnummer;

public class JPAVedtakSpec {
    public static Specification<JPAVedtakInnslag> erGyldig() {
        return (innslag, cq, cb) -> {
            var til = innslag.get(gyldigTil);
            return cb.or(cb.greaterThanOrEqualTo(til, now()), cb.isNull(til));
        };
    }

    public static Specification<JPAVedtakInnslag> harFnr(Fødselsnummer id) {
        return (innslag, cq, cb) -> cb.equal(innslag.get(fnr), id);
    }
}
