package no.nav.foreldrepenger.historikk.http.interceptors;

import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

@Component
public class TimingAndLoggingClientHttpRequestInterceptor implements ClientHttpRequestInterceptor {
    private static final Logger LOG = LoggerFactory.getLogger(TimingAndLoggingClientHttpRequestInterceptor.class);

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
            throws IOException {

        var uri = UriComponentsBuilder.fromHttpRequest(request).replaceQuery(null).build().toUri();
        LOG.info("{} - {}", request.getMethod(), uri);
        var timer = new StopWatch();
        timer.start();
        var respons = execution.execute(request, body);
        timer.stop();
        if (hasError(respons.getStatusCode())) {
            LOG.warn("{} - {} - ({}). Dette tok {}ms", request.getMethod(), request.getURI(),
                    respons.getRawStatusCode(), timer.getTime(MILLISECONDS));
        } else {
            LOG.info("{} - {} - ({}). Dette tok {}ms", request.getMethod(), uri,
                    respons.getStatusCode(), timer.getTime(MILLISECONDS));
        }
        return respons;
    }

    protected boolean hasError(HttpStatusCode code) {
        return code.is4xxClientError() || code.is5xxServerError();
    }
}
