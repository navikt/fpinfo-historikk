package no.nav.foreldrepenger.historikk.http;

import java.net.URI;

import org.springframework.http.HttpHeaders;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.client.HttpClientErrorException.Forbidden;
import org.springframework.web.client.HttpServerErrorException.BadGateway;
import org.springframework.web.client.HttpServerErrorException.InternalServerError;
import org.springframework.web.client.ResourceAccessException;

@Retryable(include = { ResourceAccessException.class,
        BadGateway.class }, exclude = { InternalServerError.class,
                Forbidden.class }, maxAttemptsExpression = "#{${rest.retry.attempts:3}}", backoff = @Backoff(delayExpression = "#{${rest.retry.delay:1000}}"))

public interface RestConnection {

    <T> T getForObject(URI uri, Class<T> responseType);

    <T> T postForEntity(URI uri, Object payload, Class<T> responseType);

    <T> T getForObject(URI uri, Class<T> responseType, boolean doThrow);

    <T> T getForEntity(URI uri, Class<T> responseType);

    <T> T getForEntity(URI uri, Class<T> responseType, boolean doThrow);

    <T> T exchangeGet(URI uri, Class<T> responseType, HttpHeaders headers);

}
