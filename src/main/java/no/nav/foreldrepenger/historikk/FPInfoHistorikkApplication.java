package no.nav.foreldrepenger.historikk;

import org.flywaydb.core.api.configuration.FluentConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.flyway.FlywayConfigurationCustomizer;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import no.nav.security.oidc.api.Unprotected;
import no.nav.security.spring.oidc.api.EnableOIDCTokenValidation;

@EnableOIDCTokenValidation(ignore = { "org.springframework", "springfox.documentation" })
@SpringBootApplication
@RestController
@Configuration
public class FPInfoHistorikkApplication {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public static void main(String[] args) {
        new SpringApplicationBuilder(FPInfoHistorikkApplication.class)
                .main(FPInfoHistorikkApplication.class)
                .run(args);
    }

    @GetMapping("/ready")
    @Unprotected
    public String ready() {
        int count = jdbcTemplate.queryForObject("SELECT COUNT(USERNAME) FROM TESTDATA", Integer.class);
        return "OK (" + count + " rows)";
    }

    @Bean
    public FlywayConfigurationCustomizer flywayConfig() {
        return new FlywayConfigurationCustomizer() {
            private final Logger LOG = LoggerFactory.getLogger(FlywayConfigurationCustomizer.class);

            @Override
            public void customize(FluentConfiguration configuration) {
                LOG.info("Init SQL set");
                configuration.initSql(String.format("SET ROLE \"%s-admin\"", "fpinfo-historikk-preprod"));
            }
        };
    }
}