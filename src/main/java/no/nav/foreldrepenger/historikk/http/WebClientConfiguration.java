package no.nav.foreldrepenger.historikk.http;

import no.nav.boot.conditionals.ConditionalOnGCP;
import no.nav.boot.conditionals.ConditionalOnLocalOrTest;
import no.nav.foreldrepenger.common.util.MDCUtil;
import no.nav.security.token.support.client.core.oauth2.OAuth2AccessTokenService;
import no.nav.security.token.support.client.spring.ClientConfigurationProperties;
import no.nav.security.token.support.client.spring.oauth2.ClientConfigurationPropertiesMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.reactive.function.client.WebClientCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.util.Optional;

import static no.nav.foreldrepenger.common.util.Constants.NAV_CALL_ID;
import static no.nav.foreldrepenger.common.util.Constants.NAV_CALL_ID1;
import static no.nav.foreldrepenger.common.util.Constants.NAV_CALL_ID2;
import static no.nav.foreldrepenger.common.util.Constants.NAV_CONSUMER_ID;
import static no.nav.foreldrepenger.common.util.TokenUtil.BEARER;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Configuration
public class WebClientConfiguration {

    @Value("${spring.application.name:fpinfo-historikk}")
    private String consumer;


    @Bean
    WebClientCustomizer globalBean(HttpClient httpClient) {
        return builder ->
            builder.clientConnector(new ReactorClientHttpConnector(httpClient))
                .filter(correlatingFilterFunction());
    }

    @Bean
    @ConditionalOnLocalOrTest
    HttpClient testClient() {
        return HttpClient.create().wiretap(true);
    }

    @Bean
    @ConditionalOnGCP
    HttpClient clusterClient() {
        return HttpClient.create();
    }

    private ExchangeFilterFunction correlatingFilterFunction() {
        return (req, next) -> next.exchange(ClientRequest.from(req)
                .header(NAV_CONSUMER_ID, consumerId())
                .header(NAV_CALL_ID, MDCUtil.callId())
                .header(NAV_CALL_ID1, MDCUtil.callId())
                .header(NAV_CALL_ID2, MDCUtil.callId())
                .build());
    }

    private String consumerId() {
        return Optional.ofNullable(MDCUtil.consumerId()).orElse(consumer);
    }

    private static ExchangeFilterFunction headerFilter(String header, String value) {
        return (req, next) -> next.exchange(ClientRequest.from(req).header(header, value).build());
    }

    @Component
    public static class TokenXExchangeFilterFunction implements ExchangeFilterFunction {

        private static final Logger LOG = LoggerFactory.getLogger(TokenXExchangeFilterFunction.class);

        private final OAuth2AccessTokenService service;
        private final ClientConfigurationPropertiesMatcher matcher;
        private final ClientConfigurationProperties configs;

        TokenXExchangeFilterFunction(ClientConfigurationProperties configs,
                                     OAuth2AccessTokenService service,
                                     ClientConfigurationPropertiesMatcher matcher) {
            this.service = service;
            this.matcher = matcher;
            this.configs = configs;
        }

        @Override
        public Mono<ClientResponse> filter(ClientRequest req, ExchangeFunction next) {
            var url = req.url();
            LOG.trace("Sjekker token exchange for {}", url);
            var config = matcher.findProperties(configs, url);
            if (config.isPresent()) {
                LOG.trace("Gjør token exchange for {} med konfig {}", url, config);
                var token = service.getAccessToken(config.get()).getAccessToken();
                LOG.info("Token exchange for {} OK", url);
                return next.exchange(ClientRequest.from(req).header(AUTHORIZATION, BEARER + token)
                    .build());
            }
            LOG.trace("Ingen token exchange for {}", url);
            return next.exchange(ClientRequest.from(req).build());
        }

        @Override
        public String toString() {
            return getClass().getSimpleName() + " [service=" + service + ", matcher=" + matcher + ", configs=" + configs + "]";
        }
    }
}
