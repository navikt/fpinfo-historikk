package no.nav.foreldrepenger.historikk.oppslag;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.vault.core.lease.LeaseEndpoints;
import org.springframework.vault.core.lease.SecretLeaseContainer;
import org.springframework.vault.core.lease.domain.RequestedSecret;
import org.springframework.vault.core.lease.event.SecretLeaseCreatedEvent;

import com.bettercloud.vault.VaultConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
@ConditionalOnProperty(value = "vault.enabled", matchIfMissing = true)
public class VaultHikariConfig implements InitializingBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(VaultConfig.class.getName());
    @Value("${spring.cloud.vault.database.backend}")
    private String vaultPostgresBackend;
    @Value("${spring.cloud.vault.database.role}")
    private String vaultPostgresRole;
    private final SecretLeaseContainer container;
    private final HikariDataSource hikariDataSource;
    @Value("${spring.database.username}")
    private String username;

    public VaultHikariConfig(SecretLeaseContainer container, HikariDataSource hikariDataSource) {
        this.container = container;
        this.hikariDataSource = hikariDataSource;
    }

    @Override
    public void afterPropertiesSet() {
        container.setLeaseEndpoints(LeaseEndpoints.SysLeases);
        RequestedSecret secret = RequestedSecret
                .rotating(this.vaultPostgresBackend + "/creds/" + this.vaultPostgresRole);
        container.addLeaseListener(leaseEvent -> {
            if (leaseEvent.getSource() == secret && leaseEvent instanceof SecretLeaseCreatedEvent) {
                LOGGER.info("Rotating creds for path: " + leaseEvent.getSource().getPath());
                SecretLeaseCreatedEvent slce = (SecretLeaseCreatedEvent) leaseEvent;
                String username = slce.getSecrets().get("username").toString();
                String password = slce.getSecrets().get("password").toString();
                LOGGER.info("Creds are {} {}", username, password);
                hikariDataSource.setUsername(username);
                hikariDataSource.setPassword(password);
                hikariDataSource.getHikariConfigMXBean().setUsername(username);
                hikariDataSource.getHikariConfigMXBean().setPassword(password);
            }
        });
        container.addRequestedSecret(secret);
    }
}
