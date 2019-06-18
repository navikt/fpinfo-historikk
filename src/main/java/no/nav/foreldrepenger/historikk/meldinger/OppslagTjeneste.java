package no.nav.foreldrepenger.historikk.meldinger;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import no.nav.foreldrepenger.historikk.domain.AktørId;

@Service
public class OppslagTjeneste {

    private final OppslagConnection connection;

    public OppslagTjeneste(OppslagConnection connection) {
        this.connection = connection;
    }

    @Cacheable(cacheNames = "aktør")
    public AktørId hentAktørId() {
        return connection.hentAktørId();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[connection=" + connection + "]";
    }

}
