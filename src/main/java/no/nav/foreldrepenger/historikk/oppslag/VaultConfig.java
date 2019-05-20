package no.nav.foreldrepenger.historikk.oppslag;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.vault.core.lease.LeaseEndpoints;
import org.springframework.vault.core.lease.SecretLeaseContainer;
import org.springframework.vault.core.lease.domain.RequestedSecret;
import org.springframework.vault.core.lease.event.SecretLeaseCreatedEvent;

import com.zaxxer.hikari.HikariDataSource;

@Configuration
@ConditionalOnProperty(value = "vault.enabled", matchIfMissing = true)
public class VaultConfig implements InitializingBean {
    private static Logger LOGGER = LoggerFactory.getLogger(VaultConfig.class.getName());
    @Value("${vault.postgres.backend}")
    private String vaultPostgresBackend;
    @Value("${vault.postgres.role}")
    private String vaultPostgresRole;
    @Autowired
    private SecretLeaseContainer container;
    @Autowired
    private HikariDataSource hikariDataSource;

    @Override
    public void afterPropertiesSet() {
        // todo Remove when
        // https://github.com/spring-cloud/spring-cloud-vault/issues/333 has been
        // resolved
        container.setLeaseEndpoints(LeaseEndpoints.SysLeases);
        RequestedSecret secret = RequestedSecret
                .rotating(this.vaultPostgresBackend + "/creds/" + this.vaultPostgresRole);
        container.addLeaseListener(leaseEvent -> {
            if (leaseEvent.getSource() == secret && leaseEvent instanceof SecretLeaseCreatedEvent) {
                LOGGER.info("Rotating creds for path: " + leaseEvent.getSource().getPath());
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
