package no.nav.foreldrepenger.historikk.tjenester.oppslag;

import no.nav.foreldrepenger.historikk.domain.AktørId;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class OppslagTjeneste implements Oppslag {

    private final OppslagConnection connection;

    public OppslagTjeneste(OppslagConnection connection) {
        this.connection = connection;
    }

    @Override
    @Cacheable(cacheNames = "aktør", keyGenerator = "autentisertFnrKeyGenerator")
    public AktørId aktørId() {
        return connection.hentAktørId();
    }

    @Override
    public String orgNavn(String orgnr) {
        return connection.orgNavn(orgnr);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[connection=" + connection + "]";
    }

}
