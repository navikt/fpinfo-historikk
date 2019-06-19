package no.nav.foreldrepenger.historikk.meldinger.dao;

import org.springframework.data.jpa.domain.Specification;

import no.nav.foreldrepenger.historikk.domain.AktørId;
import no.nav.foreldrepenger.historikk.meldinger.event.SøknadType;

public class MinidialogSpec {

    private MinidialogSpec() {

    }

    public static Specification<JPAMinidialogInnslag> harAktør(AktørId aktør) {
        return (innslag, cq, cb) -> cb.equal(innslag.get("aktørId"), aktør.getAktørId());
    }

    public static Specification<JPAMinidialogInnslag> harHandling(SøknadType type) {
        return (innslag, cq, cb) -> cb.equal(innslag.get("handling"), type.name());
    }

}
