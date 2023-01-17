package no.nav.foreldrepenger.historikk.tjenester.oppslag;

import no.nav.foreldrepenger.historikk.http.TokenXExchangeFilterFunction;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import static no.nav.foreldrepenger.historikk.tjenester.oppslag.OppslagConnectionWebclient.OPPSLAG;

@Configuration
public class OppslagWebclient {

    @Qualifier(OPPSLAG)
    @Bean
    WebClient webClient(WebClient.Builder builder,
                        TokenXExchangeFilterFunction tokenXExchangeFilterFunction,
                        OppslagConfig oppslagConfig) {
         return builder.baseUrl(oppslagConfig.getBaseUri().toASCIIString())
                      .filter(tokenXExchangeFilterFunction)
                      .build();
    }


}
