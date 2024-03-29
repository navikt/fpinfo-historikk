package no.nav.foreldrepenger.historikk;

import static no.nav.boot.conditionals.Cluster.profiler;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.retry.annotation.EnableRetry;

import no.nav.security.token.support.client.spring.oauth2.EnableOAuth2Client;
import no.nav.security.token.support.spring.api.EnableJwtTokenValidation;

@SpringBootApplication
@ConfigurationPropertiesScan
@EnableCaching
@EnableRetry
@EnableKafka
@EnableJpaAuditing
@EnableOAuth2Client(cacheEnabled = true)
@EnableJwtTokenValidation(ignore = { "org.springframework", "org.springdoc" })
public class FPInfoHistorikkApplication {
    public static void main(String[] args) {

        new SpringApplicationBuilder(FPInfoHistorikkApplication.class)
                .profiles(profiler())
                .main(FPInfoHistorikkApplication.class)
                .run(args);
    }
}
