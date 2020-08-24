package no.nav.foreldrepenger.historikk.http;

import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;

@Retryable(include = {
        RestClientException.class,
}, exclude = {
        HttpClientErrorException.NotFound.class,
        HttpClientErrorException.Forbidden.class,
        HttpClientErrorException.BadRequest.class,
        HttpClientErrorException.Unauthorized.class,
}, maxAttemptsExpression = "#{${rest.retry.attempts:3}}", backoff = @Backoff(delayExpression = "#{${rest.retry.delay:1000}}"))

public interface RetryAware {

}