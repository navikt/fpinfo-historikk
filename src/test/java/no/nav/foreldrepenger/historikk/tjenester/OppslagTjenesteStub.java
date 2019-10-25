package no.nav.foreldrepenger.historikk.tjenester;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import no.nav.foreldrepenger.historikk.domain.AktørId;
import no.nav.foreldrepenger.historikk.tjenester.oppslag.Oppslag;

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

    @Override
    public String personNavn(AktørId id) {
        return "Ole Olsen";
    }

}
