package no.nav.foreldrepenger.historikk.config;

import org.springframework.boot.autoconfigure.flyway.FlywayConfigurationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FlywayConfig {
    @Bean
    public FlywayConfigurationCustomizer flywayConfig() {
        return c -> c.initSql("SET ROLE \"fpinfo-historikk-preprod-admin\"");
    }
}
