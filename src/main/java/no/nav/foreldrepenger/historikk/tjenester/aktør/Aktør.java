package no.nav.foreldrepenger.historikk.tjenester.aktør;

import no.nav.foreldrepenger.historikk.domain.AktørId;
import no.nav.foreldrepenger.historikk.http.Pingable;

public interface Aktør extends Pingable {

    AktørId aktørId();

}
