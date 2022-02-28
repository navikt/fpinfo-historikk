package no.nav.foreldrepenger.historikk.config;

import static java.util.Collections.singletonList;
import static no.nav.foreldrepenger.boot.conditionals.EnvUtil.LOCAL;
import static org.springframework.retry.RetryContext.NAME;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
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

@Configuration
public class RestClientConfiguration {

    private static final Logger LOG = LoggerFactory.getLogger(RestClientConfiguration.class);

    @Bean
    @Primary
    public RestOperations restTemplate(RestTemplateBuilder builder,
            TokenExchangeClientRequestInterceptor tokenxInterceptor,
            TimingAndLoggingClientHttpRequestInterceptor timingInterceptor,
            MDCValuesPropagatingClienHttpRequesInterceptor mdcInterceptor) {
        LOG.info("Registrerer interceptorer {},{},{} for ikke-STS", tokenxInterceptor, timingInterceptor,
            mdcInterceptor);
        return builder
                .interceptors(tokenxInterceptor, timingInterceptor, mdcInterceptor)
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
