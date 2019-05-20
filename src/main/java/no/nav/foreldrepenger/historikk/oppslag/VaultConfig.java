package no.nav.foreldrepenger.historikk.oppslag;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.vault.config.databases.VaultDatabaseProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.vault.core.lease.SecretLeaseContainer;
import org.springframework.vault.core.lease.domain.RequestedSecret;
import org.springframework.vault.core.lease.event.SecretLeaseCreatedEvent;

import com.zaxxer.hikari.HikariDataSource;

@Configuration
@ConditionalOnProperty(value = "vault.enabled", matchIfMissing = true)
public class VaultConfig implements InitializingBean {
    @Autowired
    private SecretLeaseContainer container;
    @Autowired
    private HikariDataSource hikariDataSource;
    @Autowired
    private VaultDatabaseProperties properties;
    private static final Logger LOG = LoggerFactory.getLogger(VaultConfig.class);

    @Override
    public void afterPropertiesSet() {
        RequestedSecret secret = RequestedSecret.rotating(properties.getBackend() + "/creds/" + properties.getRole());
        container.addLeaseListener(leaseEvent -> {
            LOG.info("Vault: Lease Event: {}", leaseEvent);
            if (leaseEvent.getSource() == secret && leaseEvent instanceof SecretLeaseCreatedEvent) {
                LOG.info("Vault: Refreshing database credentials. Lease Event: {}", leaseEvent);
                SecretLeaseCreatedEvent slce = (SecretLeaseCreatedEvent) leaseEvent;
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
