package no.nav.foreldrepenger.historikk.http;

import no.nav.security.token.support.client.core.oauth2.OAuth2AccessTokenService;
import no.nav.security.token.support.client.spring.ClientConfigurationProperties;
import no.nav.security.token.support.client.spring.oauth2.ClientConfigurationPropertiesMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import reactor.core.publisher.Mono;

import static no.nav.foreldrepenger.common.util.TokenUtil.BEARER;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Component
public class TokenXExchangeFilterFunction implements ExchangeFilterFunction {

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
        var hostname = url.getHost();
        LOG.trace("Sjekker token exchange for {}", hostname);
        var config = matcher.findProperties(configs, url);
        if (config.isPresent()) {
            LOG.trace("Gjør token exchange for {} med konfig {}", hostname, config);
            var token = service.getAccessToken(config.get()).getAccessToken();
            LOG.info("Token exchange for {} OK", hostname);
            return next.exchange(ClientRequest.from(req).header(AUTHORIZATION, BEARER + token)
                                              .build());
        }
        LOG.trace("Ingen token exchange for {}", hostname);
        return next.exchange(ClientRequest.from(req).build());
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [service=" + service + ", matcher=" + matcher + ", configs=" + configs + "]";
    }
}
