package no.nav.foreldrepenger.historikk.oppslag;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.vault.config.databases.VaultDatabaseProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.vault.core.lease.LeaseEndpoints;
import org.springframework.vault.core.lease.SecretLeaseContainer;
import org.springframework.vault.core.lease.domain.RequestedSecret;
import org.springframework.vault.core.lease.event.SecretLeaseCreatedEvent;

import com.zaxxer.hikari.HikariDataSource;

@Configuration
@ConditionalOnProperty(value = "vault.enabled", matchIfMissing = true)
public class VaultHikariConfig implements InitializingBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(VaultHikariConfig.class.getName());
    private final SecretLeaseContainer container;
    private final HikariDataSource hikariDataSource;
    @Autowired
    private VaultDatabaseProperties properties;

    public VaultHikariConfig(SecretLeaseContainer container, HikariDataSource hikariDataSource) {
        this.container = container;
        this.hikariDataSource = hikariDataSource;
    }

    @Override
    public void afterPropertiesSet() {
        container.setLeaseEndpoints(LeaseEndpoints.SysLeases);
        RequestedSecret secret = RequestedSecret
                .rotating(properties.getBackend() + "/creds/" + properties.getRole());
        container.addLeaseListener(leaseEvent -> {
            if (leaseEvent.getSource() == secret && leaseEvent instanceof SecretLeaseCreatedEvent) {
                LOGGER.info("Rotating creds for path: " + leaseEvent.getSource().getPath());
                SecretLeaseCreatedEvent slce = SecretLeaseCreatedEvent.class.cast(leaseEvent);
                String username = slce.getSecrets().get("username").toString();
                String password = slce.getSecrets().get("password").toString();
                hikariDataSource.setUsername(username);
                hikariDataSource.setPassword(password);
                hikariDataSource.getHikariConfigMXBean().setUsername(username);
                hikariDataSource.getHikariConfigMXBean().setPassword(password);
            }
        });
        container.addRequestedSecret(secret);
    }
}
