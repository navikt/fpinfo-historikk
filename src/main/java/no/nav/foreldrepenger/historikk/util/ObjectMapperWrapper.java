package no.nav.foreldrepenger.historikk.util;

import java.io.IOException;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import no.nav.foreldrepenger.historikk.error.UnexpectedInputException;

@Component
public class ObjectMapperWrapper {

    private final ObjectMapper delegate;

    public ObjectMapperWrapper(ObjectMapper delegate) {
        this.delegate = delegate;
    }

    public <T> T readValue(String json, Class<T> clazz) {
        try {
            return delegate.readValue(json, clazz);
        } catch (IOException e) {
            throw new UnexpectedInputException("Kunne ikke deserialisere melding fra %s til %s", e, json, clazz);
        }
    }

    public String writeValueAsString(Object obj) {
        try {
            return delegate.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (IOException e) {
            throw new UnexpectedInputException("Kunne ikke serialisere melding fra %s", e, obj);
        }
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [delegate=" + delegate + "]";
    }
}
