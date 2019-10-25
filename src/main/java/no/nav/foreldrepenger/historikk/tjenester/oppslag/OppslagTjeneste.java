package no.nav.foreldrepenger.historikk.tjenester.oppslag;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
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
    @Cacheable(cacheNames = "aktør")
    @Retryable(value = {
            HttpServerErrorException.class }, maxAttemptsExpression = "#{${oppslag.aktør.attempts:3}}", backoff = @Backoff(delayExpression = "#{${oppslag.aktør.delay:500}}"))

    public AktørId aktørId() {
        return connection.hentAktørId();
    }

    @Cacheable(cacheNames = "fnr")
    @Retryable(value = {
            HttpServerErrorException.class }, maxAttemptsExpression = "#{${oppslag.org.attempts:3}}", backoff = @Backoff(delayExpression = "#{${oppslag.org.delay:500}}"))
    public Fødselsnummer fnr(AktørId aktørId) {
        return connection.hentFnr(aktørId);
    }

    @Override
    @Cacheable(cacheNames = "fnr")
    @Retryable(value = {
            HttpServerErrorException.class }, maxAttemptsExpression = "#{${oppslag.person.attempts:3}}", backoff = @Backoff(delayExpression = "#{${oppslag.person.delay:500}}"))
    public String personNavn(AktørId aktørId) {
        try {
            return connection.hentNavn(aktørId);
        } catch (HttpClientErrorException.Unauthorized e) {
            return null;
        }
    }

    @Cacheable(cacheNames = "organisasjon")
    @Retryable(value = {
            HttpServerErrorException.class }, maxAttemptsExpression = "#{${oppslag.org.attempts:3}}", backoff = @Backoff(delayExpression = "#{${oppslag.org.delay:500}}"))
    @Override
    public String orgNavn(String orgnr) {
        try {
            return connection.orgNavn(orgnr);
        } catch (HttpClientErrorException.Unauthorized e) {
            return null;
        }
    }

    @Recover
    public String personNavnRecovery(HttpServerErrorException e, Fødselsnummer fnr) {
        LOG.info("Retry failure recovery fnr");
        return null;
    }

    @Recover
    public String orgNavnRecovery(HttpClientErrorException.Unauthorized e, String orgnr) {
        LOG.info("Retry failure recovery orgnr");
        return null;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[connection=" + connection + "]";
    }

}
