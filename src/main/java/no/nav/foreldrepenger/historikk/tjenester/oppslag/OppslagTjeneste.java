package no.nav.foreldrepenger.historikk.tjenester.oppslag;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import no.nav.foreldrepenger.historikk.domain.AktørId;
import no.nav.foreldrepenger.historikk.domain.Fødselsnummer;

@Service
public class OppslagTjeneste {

    private final OppslagConnection connection;

    public OppslagTjeneste(OppslagConnection connection) {
        this.connection = connection;
    }

    public AktørId aktørId() {
        return connection.hentAktørId();
    }

    public Fødselsnummer fnr(AktørId aktørId) {
        return connection.hentFnr(aktørId);
    }

    public String personNavn(Fødselsnummer fnr) {
        return connection.hentNavn(fnr);
    }

    @Cacheable(cacheNames = "organisasjon")
    public String orgNavn(String orgnr) {
        return connection.orgNavn(orgnr);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[connection=" + connection + "]";
    }

}
