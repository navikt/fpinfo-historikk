package no.nav.foreldrepenger.historikk.http;

import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.reactive.function.client.WebClientResponseException;


@Retryable(exclude = {
    WebClientResponseException.NotFound.class,
    WebClientResponseException.UnsupportedMediaType.class,
    WebClientResponseException.UnprocessableEntity.class,
    WebClientResponseException.BadRequest.class,
    WebClientResponseException.Forbidden.class,
    WebClientResponseException.Unauthorized.class
}, maxAttemptsExpression = "#{${rest.retry.attempts:2}}", backoff = @Backoff(delayExpression = "#{${rest.retry.delay:500}}"))
public interface WebClientRetryAware {

}
