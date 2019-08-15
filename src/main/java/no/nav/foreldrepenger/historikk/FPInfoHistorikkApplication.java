package no.nav.foreldrepenger.historikk;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.kafka.annotation.EnableKafka;

import no.nav.foreldrepenger.historikk.config.ClusterAwareSpringProfileResolver;
import no.nav.security.spring.oidc.api.EnableOIDCTokenValidation;

@EnableOIDCTokenValidation(ignore = { "org.springframework", "springfox.documentation" })
@SpringBootApplication
@EnableKafka
@EnableCaching
@EnableJpaAuditing
public class FPInfoHistorikkApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder(FPInfoHistorikkApplication.class)
                .profiles(new ClusterAwareSpringProfileResolver().getProfile())
                .main(FPInfoHistorikkApplication.class)
                .run(args);
    }
}