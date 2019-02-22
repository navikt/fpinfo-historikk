package no.nav.foreldrepenger.historikk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import no.nav.security.spring.oidc.api.EnableOIDCTokenValidation;

@EnableOIDCTokenValidation(ignore = { "org.springframework", "springfox.documentation" })
@SpringBootApplication
public class FPInfoApplication {

    public static void main(String[] args) {
        SpringApplication.run(FPInfoApplication.class, args);
    }
}