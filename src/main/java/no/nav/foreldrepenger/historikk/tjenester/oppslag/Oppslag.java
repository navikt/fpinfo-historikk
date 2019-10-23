package no.nav.foreldrepenger.historikk.tjenester.oppslag;

import no.nav.foreldrepenger.historikk.domain.AktørId;

public interface Oppslag {

    AktørId aktørId();

    String orgNavn(String arbeidsgiver);

}
