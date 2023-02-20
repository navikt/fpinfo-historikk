package no.nav.foreldrepenger.historikk.tjenester.dokumentarkiv;

import no.nav.foreldrepenger.historikk.http.TokenXExchangeFilterFunction;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import static no.nav.foreldrepenger.historikk.tjenester.dokumentarkiv.ArkivConnection.SAF;

@Configuration
public class SafWebclient {

    @Qualifier(SAF)
    @Bean
    WebClient safClient(WebClient.Builder builder,
                        TokenXExchangeFilterFunction tokenXExchangeFilterFunction,
                        ArkivOppslagConfig oppslagConfig) {
        return builder.baseUrl(oppslagConfig.getBaseUri().toASCIIString())
                      .filter(tokenXExchangeFilterFunction)
                      .codecs(c -> c.defaultCodecs().maxInMemorySize(1024 * 1024 * 100))
                      .build();
    }

}
