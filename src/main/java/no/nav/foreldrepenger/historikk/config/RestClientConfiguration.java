package no.nav.foreldrepenger.historikk.config;

import static no.nav.foreldrepenger.historikk.util.EnvUtil.DEV;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestOperations;

import no.nav.security.oidc.context.OIDCRequestContextHolder;
import no.nav.security.oidc.context.OIDCValidationContext;
import no.nav.security.spring.oidc.SpringOIDCRequestContextHolder;

@Configuration
public class RestClientConfiguration {
    private static final Logger LOG = LoggerFactory.getLogger(RestClientConfiguration.class);

    @Bean
    @Primary
    public RestOperations restTemplate(RestTemplateBuilder builder, ClientHttpRequestInterceptor... interceptors) {
        LOG.info("Registrerer interceptorer {}", Arrays.toString(interceptors));
        return builder
                .interceptors(interceptors)
                .build();
    }

    @Bean
    @Profile(DEV)
    @ConditionalOnMissingBean(SpringOIDCRequestContextHolder.class)
    OIDCRequestContextHolder dummyContextHolderForDev() {
        return new OIDCRequestContextHolder() {

            @Override
            public void setRequestAttribute(String name, Object value) {

            }

            @Override
            public void setOIDCValidationContext(OIDCValidationContext oidcValidationContext) {

            }

            @Override
            public Object getRequestAttribute(String name) {
                return null;
            }

            @Override
            public OIDCValidationContext getOIDCValidationContext() {
                return null;
            }
        };
    }

}
