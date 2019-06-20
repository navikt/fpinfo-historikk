package no.nav.foreldrepenger.historikk.tjenester.minidialog.dao;

import static java.time.LocalDate.now;
import static no.nav.foreldrepenger.historikk.tjenester.minidialog.dao.JPAMinidialogInnslag_.aktiv;
import static no.nav.foreldrepenger.historikk.tjenester.minidialog.dao.JPAMinidialogInnslag_.aktørId;
import static no.nav.foreldrepenger.historikk.tjenester.minidialog.dao.JPAMinidialogInnslag_.gyldigTil;
import static no.nav.foreldrepenger.historikk.tjenester.minidialog.dao.JPAMinidialogInnslag_.handling;

import org.springframework.data.jpa.domain.Specification;

import no.nav.foreldrepenger.historikk.domain.AktørId;
import no.nav.foreldrepenger.historikk.tjenester.innsending.SøknadType;

public final class MinidialogSpec {

    private MinidialogSpec() {

    }

    public static Specification<JPAMinidialogInnslag> erGyldig() {
        return (innslag, cq, cb) -> cb.greaterThanOrEqualTo(innslag.get(gyldigTil), now());
    }

    public static Specification<JPAMinidialogInnslag> erAktiv() {
        return (innslag, cq, cb) -> cb.isTrue(innslag.get(aktiv));
    }

    public static Specification<JPAMinidialogInnslag> harAktør(AktørId aktør) {
        return (innslag, cq, cb) -> cb.equal(innslag.get(aktørId), aktør.getAktørId());
    }

    public static Specification<JPAMinidialogInnslag> harHandling(SøknadType type) {
        return (innslag, cq, cb) -> cb.equal(innslag.get(handling), type.name());
    }

}
