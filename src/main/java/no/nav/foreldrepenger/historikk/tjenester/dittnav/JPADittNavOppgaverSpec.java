package no.nav.foreldrepenger.historikk.tjenester.dittnav;

import static no.nav.foreldrepenger.historikk.tjenester.dittnav.JPADittNavOppgave_.referanseId;

import org.springframework.data.jpa.domain.Specification;

public final class JPADittNavOppgaverSpec {

    private JPADittNavOppgaverSpec() {

    }

    public static Specification<JPADittNavOppgave> harSendtOppgave(String id) {
        return (innslag, cq, cb) -> cb.equal(innslag.get(referanseId), id);
    }
}
