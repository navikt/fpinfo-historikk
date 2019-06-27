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

public abstract class AbstractRestConnection {
    private static final String RESPONS = "Respons: {}";
    private final RestOperations restOperations;
    private final boolean isEnabled;
    private static final Logger LOG = LoggerFactory.getLogger(AbstractRestConnection.class);

    public AbstractRestConnection(RestOperations restOperations, boolean isEnabled) {
        this.restOperations = restOperations;
        this.isEnabled = isEnabled;
    }

    protected String ping(URI uri) {
        if (isEnabled) {
            return getForObject(uri, String.class);
        }
        return "OK";
    }

    protected <T> T getForObject(URI uri, Class<T> responseType) {
        return isEnabled ? getForObject(uri, responseType, false) : null;
    }

    protected <T> ResponseEntity<T> postForEntity(URI uri, HttpEntity<?> payload, Class<T> responseType) {
        if (!isEnabled) {
            return null;
        }
        ResponseEntity<T> respons = restOperations.postForEntity(uri, payload, responseType);
        if (respons.hasBody()) {
            LOG.trace(CONFIDENTIAL, "Respons: {}", respons.getBody());
        }
        return respons;
    }

    protected <T> T getForObject(URI uri, Class<T> responseType, boolean doThrow) {
        if (!isEnabled) {
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

    protected <T> ResponseEntity<T> getForEntity(URI uri, Class<T> responseType) {
        return getForEntity(uri, responseType, true);
    }

    protected <T> ResponseEntity<T> getForEntity(URI uri, Class<T> responseType, boolean doThrow) {
        try {
            ResponseEntity<T> respons = restOperations.getForEntity(uri, responseType);
            if (respons.hasBody()) {
                LOG.trace(CONFIDENTIAL, RESPONS, respons.getBody());
            }
            return respons;
        } catch (HttpClientErrorException e) {
            if (NOT_FOUND.equals(e.getStatusCode()) && !doThrow) {
                LOG.info("Fant ingen entity, returnerer null");
                return null;
            }
            throw e;
        }
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [restOperations=" + restOperations + "]";
    }
}
