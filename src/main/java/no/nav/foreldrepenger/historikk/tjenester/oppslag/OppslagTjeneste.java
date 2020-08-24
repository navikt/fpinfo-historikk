package no.nav.foreldrepenger.historikk.tjenester.oppslag;

import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import no.nav.foreldrepenger.historikk.domain.AktørId;
import no.nav.foreldrepenger.historikk.domain.Fødselsnummer;

@Service
public class OppslagTjeneste implements Oppslag {

    private final OppslagConnection connection;

    public OppslagTjeneste(OppslagConnection connection) {
        this.connection = connection;
    }

    @Override
    public AktørId aktørId() {
        return connection.hentAktørId();
    }

    @Override
    public Fødselsnummer fnr(AktørId aktørId) {
        return connection.hentFnr(aktørId);
    }

    @Override
    public String personNavn(AktørId aktørId) {
        try {
            return connection.hentNavn(aktørId);
        } catch (HttpClientErrorException.Unauthorized e) {
            return null;
        }
    }

    @Override
    public String orgNavn(String orgnr) {
        try {
            return connection.orgNavn(orgnr);
        } catch (HttpClientErrorException.Unauthorized e) {
            return null;
        }
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[connection=" + connection + "]";
    }

}
