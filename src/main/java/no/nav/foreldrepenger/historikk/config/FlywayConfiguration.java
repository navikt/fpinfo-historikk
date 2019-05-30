package no.nav.foreldrepenger.historikk.config;

import static no.nav.foreldrepenger.historikk.util.EnvUtil.PREPROD;

import org.springframework.boot.autoconfigure.flyway.FlywayConfigurationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class FlywayConfiguration {
    @Bean
    @Profile({ PREPROD })
    public FlywayConfigurationCustomizer flywayConfig() {
        return c -> c.initSql("SET ROLE \"fpinfo-historikk-preprod-admin\"");
    }
}