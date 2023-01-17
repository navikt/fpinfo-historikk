package no.nav.foreldrepenger.historikk.http;

import no.nav.boot.conditionals.ConditionalOnFSS;
import no.nav.boot.conditionals.ConditionalOnLocalOrTest;
import no.nav.foreldrepenger.common.util.MDCUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.reactive.function.client.WebClientCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import reactor.netty.http.client.HttpClient;

import java.util.Optional;

import static no.nav.foreldrepenger.common.util.Constants.NAV_CALL_ID;
import static no.nav.foreldrepenger.common.util.Constants.NAV_CALL_ID1;
import static no.nav.foreldrepenger.common.util.Constants.NAV_CALL_ID2;
import static no.nav.foreldrepenger.common.util.Constants.NAV_CONSUMER_ID;

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
    @ConditionalOnFSS
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

}
