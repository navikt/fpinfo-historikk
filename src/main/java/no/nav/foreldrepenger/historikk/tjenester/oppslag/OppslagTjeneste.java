package no.nav.foreldrepenger.historikk.tjenester.oppslag;

import org.springframework.stereotype.Service;

import no.nav.foreldrepenger.historikk.domain.AktørId;

@Service
public class OppslagTjeneste {

    private final OppslagConnection connection;

    public OppslagTjeneste(OppslagConnection connection) {
        this.connection = connection;
    }

    public AktørId hentAktørId() {
        return connection.hentAktørId();
    }

    public String hentNavn(String fnr) {
        return connection.hentNavn(fnr);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[connection=" + connection + "]";
    }

}
