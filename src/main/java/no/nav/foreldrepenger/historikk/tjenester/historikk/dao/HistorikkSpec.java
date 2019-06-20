package no.nav.foreldrepenger.historikk.tjenester.historikk.dao;

import static no.nav.foreldrepenger.historikk.tjenester.historikk.dao.JPAHistorikkInnslag_.aktørId;
import static no.nav.foreldrepenger.historikk.tjenester.historikk.dao.JPAHistorikkInnslag_.opprettet;

import java.time.LocalDate;

import org.springframework.data.jpa.domain.Specification;

import no.nav.foreldrepenger.historikk.domain.AktørId;

public final class HistorikkSpec {

    private HistorikkSpec() {

    }

    public static Specification<JPAHistorikkInnslag> harAktør(AktørId aktør) {
        return (innslag, cq, cb) -> cb.equal(innslag.get(aktørId), aktør.getAktørId());
    }

    public static Specification<JPAHistorikkInnslag> erEtterEllerPå(LocalDate dato) {
        return (innslag, cq, cb) -> cb.greaterThanOrEqualTo(innslag.get(opprettet), dato.atStartOfDay());
    }

}
