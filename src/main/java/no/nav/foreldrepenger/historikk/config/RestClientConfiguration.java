package no.nav.foreldrepenger.historikk.config;

import static java.util.Collections.singletonList;
import static no.nav.foreldrepenger.boot.conditionals.EnvUtil.LOCAL;
import static no.nav.foreldrepenger.historikk.config.Constants.TOKENX;
import static org.springframework.retry.RetryContext.NAME;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.RetryListener;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.client.RestOperations;

import no.nav.foreldrepenger.historikk.http.ClientPropertiesFinder;
import no.nav.foreldrepenger.historikk.http.MDCValuesPropagatingClienHttpRequesInterceptor;
import no.nav.foreldrepenger.historikk.http.TimingAndLoggingClientHttpRequestInterceptor;
import no.nav.foreldrepenger.historikk.http.TokenExchangeClientRequestInterceptor;
import no.nav.security.token.support.core.context.TokenValidationContext;
import no.nav.security.token.support.core.context.TokenValidationContextHolder;
import no.nav.security.token.support.spring.SpringTokenValidationContextHolder;
import no.nav.security.token.support.spring.validation.interceptor.BearerTokenClientHttpRequestInterceptor;

@Configuration
public class RestClientConfiguration {

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

    @Bean
    @Qualifier(TOKENX)
    public RestOperations tokenXTemplate(RestTemplateBuilder builder,
            TokenExchangeClientRequestInterceptor tokenX,
            TimingAndLoggingClientHttpRequestInterceptor timing,
            MDCValuesPropagatingClienHttpRequesInterceptor mdc) {
        return builder
                .interceptors(tokenX, timing, mdc)
                .build();
    }

    @Bean
    @Profile(LOCAL)
    @ConditionalOnMissingBean(SpringTokenValidationContextHolder.class)
    TokenValidationContextHolder dummyContextHolderForLocal() {
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

    @Bean
    public ClientPropertiesFinder propertiesFinder() {
        return (configs, req) -> {
            LOG.info("Slår opp token X konfig for {}", req.getHost());
            return configs.getRegistration().get(req.getHost());
        };
    }

    @Bean
    public List<RetryListener> retryListeners() {
        List<RetryListener> listener = singletonList(new RetryListener() {

            @Override
            public <T, E extends Throwable> void onError(RetryContext ctx, RetryCallback<T, E> cb,
                    Throwable throwable) {
                LOG.warn("Metode {} kastet exception {} for {}. gang",
                        ctx.getAttribute(NAME), throwable.toString(), ctx.getRetryCount());
            }

            @Override
            public <T, E extends Throwable> void close(RetryContext ctx, RetryCallback<T, E> cb, Throwable t) {
                if (t != null) {
                    LOG.warn("Metode {} avslutter ikke-vellykket retry etter {}. forsøk grunnet {}",
                            ctx.getAttribute(NAME), ctx.getRetryCount(), t.toString(), t);
                } else {
                    if (ctx.getRetryCount() > 0) {
                        LOG.info("Metode {} avslutter vellykket retry etter {}. forsøk",
                                ctx.getAttribute(NAME), ctx.getRetryCount());
                    }
                }
            }

            @Override
            public <T, E extends Throwable> boolean open(RetryContext ctx, RetryCallback<T, E> cb) {
                var labelField = ReflectionUtils.findField(cb.getClass(), "val$label");
                ReflectionUtils.makeAccessible(labelField);
                String metode = (String) ReflectionUtils.getField(labelField, cb);
                if (ctx.getRetryCount() > 0) {
                    LOG.info("Metode {} gjør retry for {}. gang", metode, ctx.getRetryCount());
                }
                return true;
            }
        });
        return listener;

    }

}
