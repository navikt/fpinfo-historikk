package no.nav.foreldrepenger.historikk.tjenester.søknad.dao;

import static no.nav.foreldrepenger.historikk.tjenester.søknad.dao.JPASøknadsHistorikkInnslag_.fnr;
import static no.nav.foreldrepenger.historikk.tjenester.søknad.dao.JPASøknadsHistorikkInnslag_.opprettet;

import java.time.LocalDate;

import org.springframework.data.jpa.domain.Specification;

import no.nav.foreldrepenger.historikk.domain.Fødselsnummer;

public final class SøknadsHistorikkSpec {

    private SøknadsHistorikkSpec() {

    }

    public static Specification<JPASøknadsHistorikkInnslag> harFnr(Fødselsnummer f) {
        return (innslag, cq, cb) -> cb.equal(innslag.get(fnr), f);
    }

    public static Specification<JPASøknadsHistorikkInnslag> erEtterEllerPå(LocalDate dato) {
        return (innslag, cq, cb) -> cb.greaterThanOrEqualTo(innslag.get(opprettet), dato.atStartOfDay());
    }

}
