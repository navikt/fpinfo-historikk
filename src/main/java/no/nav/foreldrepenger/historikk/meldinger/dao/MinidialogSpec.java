package no.nav.foreldrepenger.historikk.meldinger.dao;

import static no.nav.foreldrepenger.historikk.meldinger.dao.JPAMinidialogInnslag_.aktiv;
import static no.nav.foreldrepenger.historikk.meldinger.dao.JPAMinidialogInnslag_.aktørId;
import static no.nav.foreldrepenger.historikk.meldinger.dao.JPAMinidialogInnslag_.gyldigTil;
import static no.nav.foreldrepenger.historikk.meldinger.dao.JPAMinidialogInnslag_.handling;

import java.time.LocalDate;

import org.springframework.data.jpa.domain.Specification;

import no.nav.foreldrepenger.historikk.domain.AktørId;
import no.nav.foreldrepenger.historikk.meldinger.event.SøknadType;

public class MinidialogSpec {

    private MinidialogSpec() {

    }

    public static Specification<JPAMinidialogInnslag> erGyldig() {
        return (innslag, cq, cb) -> cb.greaterThanOrEqualTo(innslag.get(gyldigTil),
                LocalDate.now());
    }

    public static Specification<JPAMinidialogInnslag> erAktiv() {
        return (innslag, cq, cb) -> cb.equal(innslag.get(aktiv), true);
    }

    public static Specification<JPAMinidialogInnslag> harAktør(AktørId aktør) {
        return (innslag, cq, cb) -> cb.equal(innslag.get(aktørId), aktør.getAktørId());
    }

    public static Specification<JPAMinidialogInnslag> harHandling(SøknadType type) {
        return (innslag, cq, cb) -> cb.equal(innslag.get(handling), type.name());
    }

}
