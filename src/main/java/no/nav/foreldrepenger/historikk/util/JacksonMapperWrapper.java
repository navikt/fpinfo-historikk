package no.nav.foreldrepenger.historikk.util;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import no.nav.foreldrepenger.historikk.errorhandling.UnexpectedInputException;

@Component
public class JacksonMapperWrapper {

    private static final Logger LOG = LoggerFactory.getLogger(JacksonMapperWrapper.class);

    private final ObjectMapper delegate;

    public JacksonMapperWrapper(ObjectMapper delegate) {
        this.delegate = delegate;
    }

    public <T> T convertTo(String json, Class<T> clazz) {
        try {
            return delegate.readValue(json, clazz);
        } catch (IOException e) {
            throw new UnexpectedInputException("Kunne ikke deserialisere melding fra %s til %s", e, json, clazz);
        }
    }

    public String writeValueAsString(Object obj) {
        try {
            return delegate.writeValueAsString(obj);
        } catch (IOException e) {
            throw new UnexpectedInputException("Kunne ikke serialisere melding fra %s", e, obj);
        }
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [delegate=" + delegate + "]";
    }
}
