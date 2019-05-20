package no.nav.foreldrepenger.historikk;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import no.nav.security.spring.oidc.api.EnableOIDCTokenValidation;

@EnableOIDCTokenValidation(ignore = { "org.springframework", "springfox.documentation" })
@SpringBootApplication
@RestController
public class FPInfoHistorikkApplication {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public static void main(String[] args) {
        new SpringApplicationBuilder(FPInfoHistorikkApplication.class)
                .main(FPInfoHistorikkApplication.class)
                .run(args);
    }

    @GetMapping("/ready")
    public String ready() {
        jdbcTemplate.execute("SELECT 1 ");
        return "OK";
    }
}