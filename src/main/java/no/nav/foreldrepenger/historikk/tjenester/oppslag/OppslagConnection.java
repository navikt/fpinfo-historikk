package no.nav.foreldrepenger.historikk.tjenester.oppslag;


import no.nav.foreldrepenger.historikk.domain.AktørId;

public interface OppslagConnection {

    AktørId hentAktørId();

    String orgNavn(String orgnr);

}
