package no.nav.foreldrepenger.historikk;

import static no.nav.foreldrepenger.historikk.config.ClusterAwareSpringProfileResolver.profiles;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.retry.annotation.EnableRetry;

import no.nav.security.token.support.spring.api.EnableJwtTokenValidation;

@ConfigurationPropertiesScan("no.nav.foreldrepenger.historikk")
@EnableJwtTokenValidation(ignore = { "org.springframework", "springfox.documentation" })
@SpringBootApplication
@EnableKafka
@EnableCaching
@EnableRetry
@EnableJpaAuditing
public class FPInfoHistorikkApplication {
    public static void main(String[] args) {

        new SpringApplicationBuilder(FPInfoHistorikkApplication.class)
                .profiles(profiles())
                .main(FPInfoHistorikkApplication.class)
                .run(args);
    }
}