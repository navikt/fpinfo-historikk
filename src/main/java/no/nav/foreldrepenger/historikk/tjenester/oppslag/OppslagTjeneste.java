package no.nav.foreldrepenger.historikk.tjenester.oppslag;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import no.nav.foreldrepenger.historikk.domain.AktørId;
import no.nav.foreldrepenger.historikk.domain.Fødselsnummer;

@Service
public class OppslagTjeneste implements Oppslag {

    private static final Logger LOG = LoggerFactory.getLogger(OppslagTjeneste.class);

    private final OppslagConnection connection;

    public OppslagTjeneste(OppslagConnection connection) {
        this.connection = connection;
    }

    @Override
    public AktørId aktørId() {
        return connection.hentAktørId();
    }

    @Cacheable(cacheNames = "fnr")
    public Fødselsnummer fnr(AktørId aktørId) {
        return connection.hentFnr(aktørId);
    }

    @Override
    @Cacheable(cacheNames = "fnr")
    public String personNavn(AktørId aktørId) {
        try {
            return connection.hentNavn(aktørId);
        } catch (HttpClientErrorException.Unauthorized e) {
            return null;
        }
    }

    @Cacheable(cacheNames = "organisasjon")
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
