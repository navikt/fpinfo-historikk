package no.nav.foreldrepenger.historikk.meldinger.historikk.dao;

import static no.nav.foreldrepenger.historikk.meldinger.historikk.dao.JPAHistorikkInnslag_.aktørId;
import static no.nav.foreldrepenger.historikk.meldinger.historikk.dao.JPAHistorikkInnslag_.opprettet;

import java.time.LocalDate;

import org.springframework.data.jpa.domain.Specification;

import no.nav.foreldrepenger.historikk.domain.AktørId;

public final class HistorikkSpec {

    private HistorikkSpec() {

    }

    public static Specification<JPAHistorikkInnslag> harAktør(AktørId aktør) {
        return (innslag, cq, cb) -> cb.equal(innslag.get(aktørId), aktør.getAktørId());
    }

    public static Specification<JPAHistorikkInnslag> erEtter(LocalDate dato) {
        return (innslag, cq, cb) -> cb.greaterThan(innslag.get(opprettet), dato.atStartOfDay());
    }

}
