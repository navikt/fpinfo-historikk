package no.nav.foreldrepenger.historikk.http;

import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;
import static org.springframework.retry.RetryContext.NAME;
import static org.springframework.util.ReflectionUtils.findField;
import static org.springframework.util.ReflectionUtils.getField;
import static org.springframework.util.ReflectionUtils.makeAccessible;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.web.client.RestTemplateBuilderConfigurer;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.RetryListener;
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
            .build();
    }

    @Bean
    public RestTemplateBuilder restTemplateBuilderCustomizer(RestTemplateBuilderConfigurer configurer) {
        return configurer.configure(new RestTemplateBuilder())
                         .setConnectTimeout(CONNECT_TIMEOUT)
                         .setReadTimeout(READ_TIMEOUT);
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
            public <T, E extends Throwable> void onError(RetryContext ctx, RetryCallback<T, E> callback, Throwable t) {
                LOG.info("Metode {} kastet exception {} for {}. gang", ctx.getAttribute(NAME), t, ctx.getRetryCount());
            }

            @Override
            public <T, E extends Throwable> void close(RetryContext ctx, RetryCallback<T, E> callback, Throwable t) {
                if (t != null) {
                    LOG.warn("Metode {} avslutter ikke-vellykket retry etter {}. forsøk ({})",
                        ctx.getAttribute(NAME), ctx.getRetryCount(), t.getMessage(), t);
                } else {
                    if (ctx.getRetryCount() > 0) {
                        LOG.info("Metode {} avslutter vellykket retry etter {}. forsøk",
                            ctx.getAttribute(NAME), ctx.getRetryCount());
                    }
                }
            }

            @Override
            public <T, E extends Throwable> boolean open(RetryContext ctx, RetryCallback<T, E> callback) {
                var labelField = findField(callback.getClass(), "val$label");
                makeAccessible(labelField);
                var m = String.class.cast(getField(labelField, callback));
                if (ctx.getRetryCount() > 0) {
                    LOG.info("Metode {} gjør retry for {}. gang", m, ctx.getRetryCount());
                }
                return true;
            }
        });
    }

}
