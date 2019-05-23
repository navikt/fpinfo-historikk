package no.nav.foreldrepenger.historikk;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.kafka.annotation.EnableKafka;

import no.nav.security.spring.oidc.api.EnableOIDCTokenValidation;

@EnableOIDCTokenValidation(ignore = { "org.springframework", "springfox.documentation" })
@SpringBootApplication
@EnableKafka
public class FPInfoHistorikkApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder(FPInfoHistorikkApplication.class)
                .main(FPInfoHistorikkApplication.class)
                .run(args);
    }
}