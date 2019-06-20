package no.nav.foreldrepenger.historikk.meldinger.dao;

import java.time.LocalDate;

import org.springframework.data.jpa.domain.Specification;

import no.nav.foreldrepenger.historikk.domain.AktørId;

public class HistorikkSpec {

    private HistorikkSpec() {

    }

    public static Specification<JPAHistorikkInnslag> harAktør(AktørId aktør) {
        return (innslag, cq, cb) -> cb.equal(innslag.get("aktørId"), aktør.getAktørId());
    }

    public static Specification<JPAHistorikkInnslag> erAktiv() {
        return (innslag, cq, cb) -> cb.equal(innslag.get("aktiv"), true);
    }

    public static Specification<JPAHistorikkInnslag> erEtter(LocalDate dato) {
        return (innslag, cq, cb) -> cb.greaterThan(innslag.get("datoMottatt"), dato.atStartOfDay());
    }

    public static Specification<JPAHistorikkInnslag> erFør(LocalDate dato) {
        return (innslag, cq, cb) -> cb.lessThan(innslag.get("datoMottatt"), dato.atStartOfDay());
    }

}
