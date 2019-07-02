package no.nav.foreldrepenger.historikk.http;

import static no.nav.foreldrepenger.historikk.util.EnvUtil.CONFIDENTIAL;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestOperations;

public abstract class AbstractRestConnection implements PingEndpointAware {
    private static final String RESPONS = "Respons: {}";
    private final RestOperations restOperations;
    private final AbstractConfig config;
    private static final Logger LOG = LoggerFactory.getLogger(AbstractRestConnection.class);

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

    protected <T> T getForObject(URI uri, Class<T> responseType) {
        return getForObject(uri, responseType, false);
    }

    protected <T> T postForEntity(URI uri, Object payload, Class<T> responseType) {
        return postForEntity(uri, new HttpEntity<>(payload), responseType);
    }

    protected <T> T postForEntity(URI uri, HttpEntity<?> payload, Class<T> responseType) {
        if (!isEnabled()) {
            LOG.info("Service er ikke aktiv, poster ikke til {}", uri);
            return null;
        }
        ResponseEntity<T> respons = restOperations.postForEntity(uri, payload, responseType);
        LOG.trace(CONFIDENTIAL, "Respons: {}", respons.getBody());
        return respons.getBody();
    }

    protected <T> T getForObject(URI uri, Class<T> responseType, boolean doThrow) {
        if (!isEnabled()) {
            LOG.info("Service er ikke aktiv, henter ikke fra {}", uri);
            return null;
        }
        try {
            T respons = restOperations.getForObject(uri, responseType);
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

    protected <T> T getForEntity(URI uri, Class<T> responseType) {
        return getForEntity(uri, responseType, true);
    }

    protected <T> T getForEntity(URI uri, Class<T> responseType, boolean doThrow) {
        if (!isEnabled()) {
            LOG.info("Service er ikke aktiv, henter ikke entitet fra {}", uri);
            return null;
        }
        try {
            ResponseEntity<T> respons = restOperations.getForEntity(uri, responseType);
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
