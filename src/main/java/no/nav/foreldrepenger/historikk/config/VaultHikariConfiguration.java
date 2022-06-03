package no.nav.foreldrepenger.historikk.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

import com.zaxxer.hikari.HikariDataSource;

@Configuration
@ConditionalOnProperty(value = "spring.cloud.vault.database.enabled")
public class VaultHikariConfiguration {
    private static final Logger LOG = LoggerFactory.getLogger(VaultHikariConfiguration.class);

    private final HikariDataSource ds;

    @Autowired
    public VaultHikariConfiguration(@Value("${spring.datasource.username}") String username,
                                    @Value("${spring.datasource.password}") String password,
                                    HikariDataSource ds) {
        this.ds = ds;
        setUsernameAndPasswordForDS(username, password);
    }

    private void setUsernameAndPasswordForDS(String username, String password) {
        if (username == null || password == null) {
            LOG.warn("Brukernavn og/eller passord er null for datasource. Noe galt med Vault?");
        }
        ds.setUsername(username);
        ds.setPassword(password);
        ds.getHikariPoolMXBean().softEvictConnections();
    }
}
