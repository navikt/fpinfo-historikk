package no.nav.foreldrepenger.historikk.http;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import no.nav.foreldrepenger.historikk.tjenester.sts.STStjeneste;

@Component
public class STSClientRequestInterceptor implements ClientHttpRequestInterceptor {

    private static final Logger LOG = LoggerFactory.getLogger(STSClientRequestInterceptor.class);
    private final STStjeneste sts;

    public STSClientRequestInterceptor(STStjeneste sts) {
        this.sts = sts;
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body,
            ClientHttpRequestExecution execution) throws IOException {
        String token = sts.accessToken();
        LOG.debug("Adding authorization header with bearer token={}" + token);
        request.getHeaders().add(AUTHORIZATION, "Bearer " + token);
        return execution.execute(request, body);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[sts=" + sts + "]";
    }
}