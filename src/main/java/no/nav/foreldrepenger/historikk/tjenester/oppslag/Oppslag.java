package no.nav.foreldrepenger.historikk.tjenester.oppslag;

import no.nav.foreldrepenger.historikk.domain.AktørId;
import no.nav.foreldrepenger.historikk.tjenester.felles.RetryAware;

public interface Oppslag extends RetryAware {

    AktørId aktørId();

    String orgNavn(String arbeidsgiver);

}
