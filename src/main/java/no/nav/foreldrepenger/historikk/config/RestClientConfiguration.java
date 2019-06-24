package no.nav.foreldrepenger.historikk.config;

import static no.nav.foreldrepenger.historikk.util.EnvUtil.DEV;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.web.client.RestOperations;

import no.nav.foreldrepenger.historikk.http.MDCValuesPropagatingClienHttpRequesInterceptor;
import no.nav.foreldrepenger.historikk.http.STSClientRequestInterceptor;
import no.nav.foreldrepenger.historikk.http.TimingAndLoggingClientHttpRequestInterceptor;
import no.nav.security.oidc.context.OIDCRequestContextHolder;
import no.nav.security.oidc.context.OIDCValidationContext;
import no.nav.security.spring.oidc.SpringOIDCRequestContextHolder;
import no.nav.security.spring.oidc.validation.interceptor.BearerTokenClientHttpRequestInterceptor;

@Configuration
public class RestClientConfiguration {

    public static final String STS = "sts";

    private static final Logger LOG = LoggerFactory.getLogger(RestClientConfiguration.class);

    @Bean
    @Primary
    public RestOperations restTemplate(RestTemplateBuilder builder,
            BearerTokenClientHttpRequestInterceptor tokenInterceptor,
            TimingAndLoggingClientHttpRequestInterceptor timingInterceptor,
            MDCValuesPropagatingClienHttpRequesInterceptor mdcInterceptor) {
        LOG.info("Registrerer interceptorer {},{},{} for ikke-STS", tokenInterceptor, timingInterceptor,
                mdcInterceptor);
        return builder
                .interceptors(tokenInterceptor, timingInterceptor, mdcInterceptor)
                .build();
    }

    @Qualifier(STS)
    @Bean
    public RestOperations stsRestTemplate(RestTemplateBuilder builder,
            @Value("${kafka.username}") String user,
            @Value("${kafka.password}") String pw,
            TimingAndLoggingClientHttpRequestInterceptor timingInterceptor,
            STSClientRequestInterceptor stsInterceptor, MDCValuesPropagatingClienHttpRequesInterceptor mdcInterceptor) {
        LOG.info("Registrerer interceptor {},{},{} for STS", stsInterceptor, timingInterceptor, mdcInterceptor);
        return builder
                .interceptors(stsInterceptor, timingInterceptor, mdcInterceptor)
                .basicAuthentication(user, pw)
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
