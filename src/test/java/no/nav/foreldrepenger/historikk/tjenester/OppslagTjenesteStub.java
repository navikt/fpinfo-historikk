package no.nav.foreldrepenger.historikk.tjenester;

import no.nav.foreldrepenger.historikk.domain.AktørId;
import no.nav.foreldrepenger.historikk.tjenester.oppslag.Oppslag;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
public class OppslagTjenesteStub implements Oppslag {

    @Override
    public AktørId aktørId() {
        return AktørId.valueOf("42");
    }

    @Override
    public String orgNavn(String arbeidsgiver) {
        return "NAV";
    }

}
