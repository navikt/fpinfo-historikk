package no.nav.foreldrepenger.historikk.tjenester.oppslag;

import no.nav.foreldrepenger.historikk.domain.AktørId;
import no.nav.foreldrepenger.historikk.domain.Fødselsnummer;

public interface Oppslag {

    AktørId aktørId();

    String orgNavn(String arbeidsgiver);

    String personNavn(AktørId aktørId);

    Fødselsnummer fnr(AktørId aktørId);

}
