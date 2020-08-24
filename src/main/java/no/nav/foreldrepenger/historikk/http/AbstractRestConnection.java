package no.nav.foreldrepenger.historikk.http;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import java.net.URI;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestOperations;

public abstract class AbstractRestConnection implements RetryAware, PingEndpointAware {
    private static final String RESPONS = "Respons: {}";
    private final RestOperations restOperations;
    private final AbstractConfig config;
    private static final Logger LOG = LoggerFactory.getLogger(AbstractRestConnection.class);
    public static final Marker CONFIDENTIAL = MarkerFactory.getMarker("CONFIDENTIAL");

    public AbstractRestConnection(RestOperations restOperations, AbstractConfig config) {
        this.restOperations = restOperations;
        this.config = config;
    }

    @Override
    public boolean isEnabled() {
        return config.isEnabled();
    }

    protected String ping(URI uri) {
        return getForObject(uri, String.class);
    }

    public <T> T getForObject(URI uri, Class<T> responseType) {
        return getForObject(uri, responseType, false);
    }

    public <T> T postForEntity(URI uri, Object payload, Class<T> responseType) {
        return postForEntity(uri, new HttpEntity<>(payload), responseType);
    }

    public <T> T getForObject(URI uri, Class<T> responseType, boolean doThrow) {
        if (!isEnabled()) {
            LOG.info("Service er ikke aktiv, henter ikke fra {}", uri);
            return null;
        }
        try {
            var respons = restOperations.getForObject(uri, responseType);
            if (respons != null) {
                LOG.trace(CONFIDENTIAL, RESPONS, respons);
            }
            return respons;
        } catch (HttpClientErrorException e) {
            if (NOT_FOUND.equals(e.getStatusCode()) && !doThrow) {
                LOG.info("Fant intet objekt, returnerer null");
                return null;
            }
            throw e;
        }
    }

    public <T> T getForEntity(URI uri, Class<T> responseType) {
        return getForEntity(uri, responseType, true);
    }

    public <T> T getForEntity(URI uri, Class<T> responseType, boolean doThrow) {
        if (!isEnabled()) {
            LOG.info("Service er ikke aktiv, henter ikke entitet fra {}", uri);
            return null;
        }
        try {
            var respons = restOperations.getForEntity(uri, responseType);
            LOG.trace(CONFIDENTIAL, RESPONS, respons.getBody());
            return respons.getBody();

        } catch (HttpClientErrorException e) {
            if (NOT_FOUND.equals(e.getStatusCode()) && !doThrow) {
                LOG.info("Fant ingen entity, returnerer null");
                return null;
            }
            throw e;
        }
    }

    public <T> T exchangeGet(URI uri, Class<T> responseType, HttpHeaders headers) {
        return exchange(uri, GET, new HttpEntity<>(headers), responseType);
    }

    private <T> T exchange(URI uri, HttpMethod method, HttpEntity<String> entity, Class<T> responseType) {
        var respons = restOperations.exchange(uri, method, entity, responseType);
        if (respons.hasBody()) {
            return respons.getBody();
        }
        return null;
    }

    protected Set<HttpMethod> optionsForAllow(URI uri) {
        return restOperations.optionsForAllow(uri);
    }

    private <T> T postForEntity(URI uri, HttpEntity<?> payload, Class<T> responseType) {
        if (!isEnabled()) {
            LOG.info("Service er ikke aktiv, poster ikke til {}", uri);
            return null;
        }
        var respons = restOperations.postForEntity(uri, payload, responseType);
        LOG.trace(CONFIDENTIAL, RESPONS, respons.getBody());
        return respons.getBody();
    }

    @Override
    public String ping() {
        return ping(pingEndpoint());
    }

    @Override
    public String name() {
        return pingEndpoint().getHost();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[restOperations=" + restOperations + ", isEnabled=" + isEnabled() + "]";
    }
}
