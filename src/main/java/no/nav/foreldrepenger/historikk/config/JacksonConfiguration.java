package no.nav.foreldrepenger.historikk.config;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import no.nav.security.token.support.client.core.oauth2.OAuth2AccessTokenResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class JacksonConfiguration {

    @Bean
    @Primary
    public ObjectMapper customObjectmapper() {
        return DefaultJsonMapper.MAPPER
            .addMixIn(OAuth2AccessTokenResponse.class, IgnoreUnknownMixin.class);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private interface IgnoreUnknownMixin {

    }

}
