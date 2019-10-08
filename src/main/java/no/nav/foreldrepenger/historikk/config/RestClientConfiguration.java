package no.nav.foreldrepenger.historikk.config;

import static no.nav.foreldrepenger.historikk.util.EnvUtil.LOCAL;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
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
import no.nav.foreldrepenger.historikk.http.TimingAndLoggingClientHttpRequestInterceptor;
import no.nav.foreldrepenger.historikk.tjenester.sts.STSClientRequestInterceptor;
import no.nav.security.token.support.core.context.TokenValidationContext;
import no.nav.security.token.support.core.context.TokenValidationContextHolder;
import no.nav.security.token.support.spring.SpringTokenValidationContextHolder;
import no.nav.security.token.support.spring.validation.interceptor.BearerTokenClientHttpRequestInterceptor;

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
        LOG.info("Registrerer interceptorer /*{},*/{},{} for ikke-STS", tokenInterceptor, timingInterceptor,
                mdcInterceptor);
        return builder
                .interceptors(tokenInterceptor, timingInterceptor, mdcInterceptor)
                .build();
    }

    @Qualifier(STS)
    @ConditionalOnProperty(name = "sts.enabled", havingValue = "true")
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
    @ConditionalOnProperty(name = "dokarkiv.enabled", havingValue = "true")
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
    @Profile(LOCAL)
    @ConditionalOnMissingBean(SpringTokenValidationContextHolder.class)
    TokenValidationContextHolder dummyContextHolderForDev() {
        return new TokenValidationContextHolder() {

            @Override
            public TokenValidationContext getTokenValidationContext() {
                return null;
            }

            @Override
            public void setTokenValidationContext(TokenValidationContext tokenValidationContext) {
            }
        };
    }

    @Bean
    @Profile(LOCAL)
    @ConditionalOnMissingBean(BearerTokenClientHttpRequestInterceptor.class)
    BearerTokenClientHttpRequestInterceptor dummyBearerTokenClientHttpRequestInterceptor(
            TokenValidationContextHolder ctx) {
        return new BearerTokenClientHttpRequestInterceptor(ctx) {

            @Override
            public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
                    throws IOException {
                return execution.execute(request, body);
            }

        };
    }

}
