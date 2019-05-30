package no.nav.foreldrepenger.historikk.config;

import static no.nav.foreldrepenger.historikk.util.EnvUtil.PREPROD;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.flyway.FlywayConfigurationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class FlywayConfiguration {

    private static final Logger LOG = LoggerFactory.getLogger(FlywayConfiguration.class);

    @Bean
    @Profile({ PREPROD })
    public FlywayConfigurationCustomizer flywayConfig(@Value("${spring.cloud.vault.database.role}") String role) {
        LOG.info("Fikk rolle " + role);
        return c -> c.initSql("SET ROLE \"fpinfo-historikk-preprod-admin\"");
    }
}