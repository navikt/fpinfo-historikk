package no.nav.foreldrepenger.historikk;

import static no.nav.foreldrepenger.historikk.config.ClusterAwareSpringProfileResolver.profiles;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.kafka.annotation.EnableKafka;

import no.nav.security.token.support.spring.api.EnableJwtTokenValidation;

@EnableJwtTokenValidation(ignore = { "org.springframework", "springfox.documentation" })
@SpringBootApplication
@EnableKafka
@EnableCaching
@EnableJpaAuditing
public class FPInfoHistorikkApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder(FPInfoHistorikkApplication.class)
                .profiles(profiles())
                .main(FPInfoHistorikkApplication.class)
                .run(args);
    }
}