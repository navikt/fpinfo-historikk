package no.nav.foreldrepenger.historikk.tjenester.tilbakekreving;

import static java.time.LocalDate.now;
import static no.nav.foreldrepenger.historikk.tjenester.felles.HendelseType.TILBAKEKREVING_SPM;
import static no.nav.foreldrepenger.historikk.tjenester.minidialog.JPAMinidialogInnslag_.aktiv;
import static no.nav.foreldrepenger.historikk.tjenester.minidialog.JPAMinidialogInnslag_.aktørId;
import static no.nav.foreldrepenger.historikk.tjenester.minidialog.JPAMinidialogInnslag_.gyldigTil;
import static no.nav.foreldrepenger.historikk.tjenester.minidialog.JPAMinidialogInnslag_.hendelse;

import org.springframework.data.jpa.domain.Specification;

import no.nav.foreldrepenger.historikk.domain.AktørId;

public final class JPATilbakekrevingSpec {

    private JPATilbakekrevingSpec() {

    }

    public static Specification<JPATilbakekrevingInnslag> erGyldig() {
        return (innslag, cq, cb) -> cb.greaterThanOrEqualTo(innslag.get(gyldigTil), now());
    }

    public static Specification<JPATilbakekrevingInnslag> gyldigErNull() {
        return (innslag, cq, cb) -> cb.isNull(innslag.get(gyldigTil));
    }

    public static Specification<JPATilbakekrevingInnslag> erAktiv() {
        return (innslag, cq, cb) -> cb.isTrue(innslag.get(aktiv));
    }

    public static Specification<JPATilbakekrevingInnslag> harAktørId(AktørId id) {
        return (innslag, cq, cb) -> cb.equal(innslag.get(aktørId), id);
    }

    public static Specification<JPATilbakekrevingInnslag> erSpørsmål() {
        return (innslag, cq, cb) -> cb.equal(innslag.get(hendelse), TILBAKEKREVING_SPM);
    }
}
