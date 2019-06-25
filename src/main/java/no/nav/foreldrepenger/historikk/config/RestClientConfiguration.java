package no.nav.foreldrepenger.historikk.config;

import static no.nav.foreldrepenger.historikk.util.EnvUtil.DEV;

import java.io.IOException;

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
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpResponse;
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
    public static final String DOKARKIV = "dokarkiv";

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
            MDCValuesPropagatingClienHttpRequesInterceptor mdcInterceptor) {
        LOG.info("Registrerer interceptorer {},{} for STS", timingInterceptor, mdcInterceptor);
        return builder
                .interceptors(timingInterceptor, mdcInterceptor)
                .basicAuthentication(user, pw)
                .build();
    }

    @Qualifier(DOKARKIV)
    @Bean
    public RestOperations dokarkivRestTemplate(RestTemplateBuilder builder,
            TimingAndLoggingClientHttpRequestInterceptor timingInterceptor,
            STSClientRequestInterceptor stsInterceptor, MDCValuesPropagatingClienHttpRequesInterceptor mdcInterceptor) {
        LOG.info("Registrerer interceptorer {},{},{} for dokarkiv", stsInterceptor, timingInterceptor, mdcInterceptor);
        return builder
                .interceptors(stsInterceptor, timingInterceptor, mdcInterceptor)
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

    @Bean
    @Profile(DEV)
    @ConditionalOnMissingBean(BearerTokenClientHttpRequestInterceptor.class)
    BearerTokenClientHttpRequestInterceptor dummyBearerTokenClientHttpRequestInterceptor(OIDCRequestContextHolder ctx) {
        return new BearerTokenClientHttpRequestInterceptor(ctx) {

            @Override
            public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
                    throws IOException {
                return execution.execute(request, body);
            }

        };
    }

}
