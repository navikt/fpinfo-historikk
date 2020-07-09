package no.nav.foreldrepenger.historikk.config;

import static org.springframework.vault.core.lease.LeaseEndpoints.SysLeases;
import static org.springframework.vault.core.lease.domain.RequestedSecret.rotating;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.vault.config.databases.VaultDatabaseProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.vault.core.lease.SecretLeaseContainer;
import org.springframework.vault.core.lease.event.SecretLeaseCreatedEvent;

import com.zaxxer.hikari.HikariDataSource;

@Configuration
@ConditionalOnProperty(value = "spring.cloud.vault.enabled")
public class VaultHikariConfiguration implements InitializingBean {
    private static final Logger LOG = LoggerFactory.getLogger(VaultHikariConfiguration.class);
    private final SecretLeaseContainer container;
    private final HikariDataSource ds;
    private final VaultDatabaseProperties props;

    public VaultHikariConfiguration(SecretLeaseContainer container, HikariDataSource ds,
            VaultDatabaseProperties props) {
        this.container = container;
        this.ds = ds;
        this.props = props;
    }

    @Override
    public void afterPropertiesSet() {
        container.setLeaseEndpoints(SysLeases);
        String path = props.getBackend() + "/creds/" + props.getRole();
        LOG.info("Henter hemmelighet fra {}", path);
        var secret = rotating(path);
        container.addLeaseListener(leaseEvent -> {
            if ((leaseEvent.getSource() == secret) && (leaseEvent instanceof SecretLeaseCreatedEvent)) {
                LOG.info("Roterer brukernavn/passord for : {}", leaseEvent.getSource().getPath());
                var secrets = SecretLeaseCreatedEvent.class.cast(leaseEvent).getSecrets();
                ds.setUsername(get("username", secrets));
                ds.setPassword(get("password", secrets));
                ds.getHikariPoolMXBean().softEvictConnections();
            }
        });
        container.addRequestedSecret(secret);
    }

    private static String get(String key, Map<String, Object> secrets) {
        return secrets.get(key).toString();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [container=" + container + ", ds=" + ds + ", props=" + props + "]";
    }
}