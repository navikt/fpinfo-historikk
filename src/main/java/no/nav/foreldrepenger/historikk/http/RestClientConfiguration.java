package no.nav.foreldrepenger.historikk.http;

import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;
import static org.springframework.retry.RetryContext.NAME;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.RetryListener;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.client.RestOperations;

import com.google.common.base.Splitter;

import no.nav.security.token.support.client.core.oauth2.OAuth2AccessTokenService;
import no.nav.security.token.support.client.spring.ClientConfigurationProperties;
import no.nav.security.token.support.client.spring.oauth2.ClientConfigurationPropertiesMatcher;
import no.nav.security.token.support.client.spring.oauth2.OAuth2ClientRequestInterceptor;

@Configuration
public class RestClientConfiguration {
    private static final Logger LOG = LoggerFactory.getLogger(RestClientConfiguration.class);
    private static final Duration READ_TIMEOUT = Duration.ofSeconds(30);
    private static final Duration CONNECT_TIMEOUT = Duration.ofSeconds(10);

    @Bean
    @Primary
    public RestOperations customRestTemplate(RestTemplateBuilder b, ClientHttpRequestInterceptor... interceptors) {
        return b
            .interceptors(interceptors)
            .setConnectTimeout(CONNECT_TIMEOUT)
            .setReadTimeout(READ_TIMEOUT)
            .build();
    }

    @Bean
    @Order(HIGHEST_PRECEDENCE)
    public OAuth2ClientRequestInterceptor tokenExchangeClientRequestInterceptor(ClientConfigurationProperties properties,
                                                                                OAuth2AccessTokenService service,
                                                                                ClientConfigurationPropertiesMatcher matcher) {
        return new OAuth2ClientRequestInterceptor(properties, service, matcher);
    }

    @Bean
    public ClientConfigurationPropertiesMatcher tokenxClientConfigMatcher() {
        return (properties, uri) -> {
            LOG.trace("Oppslag token X konfig for {}", uri.getHost());
            var cfg = properties.getRegistration().get(Splitter.on(".").splitToList(uri.getHost()).get(0));
            if (cfg != null) {
                LOG.trace("Oppslag token X konfig for {} OK", uri.getHost());
            } else {
                LOG.trace("Oppslag token X konfig for {} fant ingenting", uri.getHost());
            }
            return Optional.ofNullable(cfg);
        };
    }

    @Bean
    public List<RetryListener> retryListeners() {
        return List.of(new RetryListener() {

            @Override
            public <T, E extends Throwable> void onError(RetryContext ctx, RetryCallback<T, E> cb,
                    Throwable throwable) {
                LOG.warn("Metode {} kastet exception {} for {}. gang",
                    ctx.getAttribute(NAME), throwable, ctx.getRetryCount());
            }

            @Override
            public <T, E extends Throwable> void close(RetryContext ctx, RetryCallback<T, E> cb, Throwable t) {
                if (t != null) {
                    LOG.warn("Metode {} avslutter ikke-vellykket retry etter {}. forsøk grunnet {}",
                            ctx.getAttribute(NAME), ctx.getRetryCount(), t.getMessage(), t);
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
    }

}