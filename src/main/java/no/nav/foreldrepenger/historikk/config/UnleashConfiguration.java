package no.nav.foreldrepenger.historikk.config;

import java.net.URI;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import no.finn.unleash.DefaultUnleash;
import no.finn.unleash.Unleash;
import no.finn.unleash.strategy.Strategy;
import no.finn.unleash.util.UnleashConfig;

@Configuration
public class UnleashConfiguration {

    private static final Logger LOG = LoggerFactory.getLogger(UnleashConfiguration.class);

    @Bean
    public Unleash unleash(
            @Value("${historikk.unleash.uri:https://unleash.nais.adeo.no/api/}") URI uri,
            @Value("${spring.application.name}") String appName,
            @Value("${historikk.instance.name:historikk-instance}") String instanceName, Strategy... strategies) {

        LOG.info("Registrerer unleash med {} og strategier {}", uri, Arrays.toString(strategies));
        return new DefaultUnleash(UnleashConfig.builder()
                .appName(appName)
                .instanceId(instanceName)
                .unleashAPI(uri)
                .build(), strategies);
    }
}
