package no.nav.foreldrepenger.historikk.config;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;

// ObjectMapper hentet fra fpsoknad-felles
public class DefaultJsonMapper {
    public static final ObjectMapper MAPPER = getObjectMapper();

    private DefaultJsonMapper() {
    }

    private static ObjectMapper getObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new Jdk8Module());
        mapper.registerModule(new JavaTimeModule());
        mapper.registerModule(new ParameterNamesModule(JsonCreator.Mode.DEFAULT));
        mapper.setDefaultPropertyInclusion(JsonInclude.Include.NON_ABSENT); // tilsvarende application.yml
        mapper.enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES); // tilsvarende application.yml
        mapper.enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS);
        mapper.enable(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_USING_DEFAULT_VALUE);
        mapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY); // tilsvarende application.yml
        mapper.enable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES); // tilsvarende application.yml
        mapper.disable(SerializationFeature.WRITE_EMPTY_JSON_ARRAYS); // tilsvarende application.yml
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS); // tilsvarende application.yml
        mapper.disable(MapperFeature.DEFAULT_VIEW_INCLUSION);
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.disable(SerializationFeature.WRITE_DURATIONS_AS_TIMESTAMPS);
        return mapper;
    }
}


