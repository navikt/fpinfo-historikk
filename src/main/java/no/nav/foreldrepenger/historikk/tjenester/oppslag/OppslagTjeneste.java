package no.nav.foreldrepenger.historikk.tjenester.oppslag;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;

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

    public Fødselsnummer fnr(AktørId aktørId) {
        return connection.hentFnr(aktørId);
    }

    @Override
    @Retryable(value = { HttpServerErrorException.class }, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    public String personNavn(Fødselsnummer fnr) {
        return connection.hentNavn(fnr);
    }

    @Cacheable(cacheNames = "organisasjon")
    @Retryable(value = { HttpServerErrorException.class }, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    @Override
    public String orgNavn(String orgnr) {
        return connection.orgNavn(orgnr);
    }

    @Recover
    public String personNavnRecovery(HttpServerErrorException e, Fødselsnummer fnr) {
        LOG.info("Retry failure recovery fnr");
        return fnr.getFnr();
    }

    @Recover
    public String orgNavnRecovery(HttpServerErrorException e, String orgnr) {
        LOG.info("Retry failure recovery orgnr");
        return orgnr;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[connection=" + connection + "]";
    }

}
