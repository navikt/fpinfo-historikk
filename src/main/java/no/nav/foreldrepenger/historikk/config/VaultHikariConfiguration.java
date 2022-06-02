package no.nav.foreldrepenger.historikk.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

import com.zaxxer.hikari.HikariDataSource;

@Configuration
@ConditionalOnProperty(value = "spring.cloud.vault.enabled")
public class VaultHikariConfiguration implements InitializingBean {
    private static final Logger LOG = LoggerFactory.getLogger(VaultHikariConfiguration.class);
    private final HikariDataSource ds;

    @Value("${spring.datasource.username}")
    private String username;
    @Value("${spring.datasource.password}")
    private String password;

    public VaultHikariConfiguration(HikariDataSource ds) {
        this.ds = ds;
    }

    @Override
    public void afterPropertiesSet() {
        LOG.info("Brukernavnet er {},", username);
        if (password == null) {
            LOG.warn("Passord er ikke satt.");
        }
        ds.setUsername(username);
        ds.setPassword(password);
        ds.getHikariPoolMXBean().softEvictConnections();
    }
}
