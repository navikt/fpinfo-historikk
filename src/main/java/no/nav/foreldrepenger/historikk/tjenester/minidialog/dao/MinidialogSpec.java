package no.nav.foreldrepenger.historikk.tjenester.minidialog.dao;

import static java.time.LocalDate.now;
import static no.nav.foreldrepenger.historikk.tjenester.minidialog.dao.JPAMinidialogInnslag_.aktiv;
import static no.nav.foreldrepenger.historikk.tjenester.minidialog.dao.JPAMinidialogInnslag_.aktørId;
import static no.nav.foreldrepenger.historikk.tjenester.minidialog.dao.JPAMinidialogInnslag_.fnr;
import static no.nav.foreldrepenger.historikk.tjenester.minidialog.dao.JPAMinidialogInnslag_.gyldigTil;
import static no.nav.foreldrepenger.historikk.tjenester.minidialog.dao.JPAMinidialogInnslag_.hendelse;

import org.springframework.data.jpa.domain.Specification;

import no.nav.foreldrepenger.historikk.domain.AktørId;
import no.nav.foreldrepenger.historikk.domain.Fødselsnummer;
import no.nav.foreldrepenger.historikk.tjenester.innsending.Hendelse;

public final class MinidialogSpec {

    private MinidialogSpec() {

    }

    public static Specification<JPAMinidialogInnslag> erGyldig() {
        return (innslag, cq, cb) -> cb.greaterThanOrEqualTo(innslag.get(gyldigTil), now());
    }

    public static Specification<JPAMinidialogInnslag> erGyldigTilNull() {
        return (innslag, cq, cb) -> cb.isNull(innslag.get(gyldigTil));
    }

    public static Specification<JPAMinidialogInnslag> erAktiv() {
        return (innslag, cq, cb) -> cb.isTrue(innslag.get(aktiv));
    }

    public static Specification<JPAMinidialogInnslag> harAktør(AktørId a) {
        return (innslag, cq, cb) -> cb.equal(innslag.get(aktørId), a);
    }

    public static Specification<JPAMinidialogInnslag> harFnr(Fødselsnummer fødselsnummer) {
        return (innslag, cq, cb) -> cb.equal(innslag.get(fnr), fødselsnummer);
    }

    public static Specification<JPAMinidialogInnslag> harHandling(Hendelse h) {
        return (innslag, cq, cb) -> cb.equal(innslag.get(hendelse), h);
    }

}
