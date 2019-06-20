package no.nav.foreldrepenger.historikk.util;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class JacksonUtil {

    private static final Logger LOG = LoggerFactory.getLogger(JacksonUtil.class);

    private final ObjectMapper mapper;

    public JacksonUtil(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public <T> T convertTo(String json, Class<T> clazz) {
        try {
            return mapper.readValue(json, clazz);
        } catch (IOException e) {
            LOG.warn("Kunne ikke deserialisere melding fra {} til {}", json, clazz, e);
            return null;
        }
    }

    public String writeValueAsString(Object obj) {
        try {
            return mapper.writeValueAsString(obj);
        } catch (IOException e) {
            LOG.warn("Kunne ikke serialisere melding fra {}", obj, e);
            return null;
        }
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [mapper=" + mapper + "]";
    }
}
