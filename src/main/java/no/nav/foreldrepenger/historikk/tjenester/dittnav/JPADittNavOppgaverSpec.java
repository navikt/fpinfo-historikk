package no.nav.foreldrepenger.historikk.tjenester.dittnav;

import org.springframework.data.jpa.domain.Specification;

import static no.nav.foreldrepenger.historikk.tjenester.dittnav.JPADittNavOppgave.NotifikasjonType.OPPGAVE;
import static no.nav.foreldrepenger.historikk.tjenester.dittnav.JPADittNavOppgave_.internReferanseId;
import static no.nav.foreldrepenger.historikk.tjenester.dittnav.JPADittNavOppgave_.sendtDone;
import static no.nav.foreldrepenger.historikk.tjenester.dittnav.JPADittNavOppgave_.type;

public final class JPADittNavOppgaverSpec {

    private JPADittNavOppgaverSpec() {

    }

    public static Specification<JPADittNavOppgave> erAktiv() {
        return (innslag, cq, cb) -> cb.isFalse(innslag.get(sendtDone));
    }

    public static Specification<JPADittNavOppgave> harReferanseId(String referanseId) {
        return (innslag, cq, cb) -> cb.equal(innslag.get(internReferanseId), referanseId);
    }

    public static Specification<JPADittNavOppgave> erOppgave() {
        return (innslag, cq, cb) -> cb.equal(innslag.get(type), OPPGAVE);
    }

}
