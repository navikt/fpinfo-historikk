package no.nav.foreldrepenger.historikk.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import io.micrometer.core.instrument.Metrics;

import lombok.extern.slf4j.Slf4j;
import no.nav.vault.jdbc.hikaricp.HikariCPVaultUtil;
import no.nav.vault.jdbc.hikaricp.VaultError;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static java.util.concurrent.TimeUnit.MINUTES;

import java.util.Properties;

@Slf4j
@Configuration
@ConditionalOnProperty(value = "vault.enabled", matchIfMissing = true)
public class DatasourceConfig {

    @Bean
    public HikariDataSource dataSource(DataSourceProperties properties, VaultConfig vaultConfig) throws VaultError {
        HikariConfig config = createHikariConfig(properties);
        if (vaultConfig.isEnabled()) {
            return HikariCPVaultUtil.createHikariDataSourceWithVaultIntegration(config, vaultConfig.getDatabaseBackend(), vaultConfig.getDatabaseRole());
        }
        config.setUsername(properties.getUsername());
        config.setPassword(properties.getPassword());
        return new HikariDataSource(config);
    }

    static HikariConfig createHikariConfig(DataSourceProperties properties) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(properties.getUrl());
        config.setMinimumIdle(2);
        config.setMaximumPoolSize(4);
        config.setConnectionTestQuery("select 1");
        config.setDriverClassName("org.postgresql.Driver");
        config.setMetricRegistry(Metrics.globalRegistry);
        config.setIdleTimeout(10001);
        config.setMaxLifetime(30001);

        var dsProperties = new Properties();
        dsProperties.setProperty("reWriteBatchedInserts", "true");
        dsProperties.setProperty("logServerErrorDetail", "false"); // skrur av batch exceptions som lekker statements i åpen logg
        config.setDataSourceProperties(dsProperties);

        return config;
    }

}
