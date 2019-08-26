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
import org.springframework.vault.core.lease.domain.RequestedSecret;
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
        RequestedSecret secret = rotating(path);
        container.addLeaseListener(leaseEvent -> {
            if ((leaseEvent.getSource() == secret) && (leaseEvent instanceof SecretLeaseCreatedEvent)) {
                LOG.info("Roterer brukernavn/passord for : {}", leaseEvent.getSource().getPath());
                Map<String, Object> secrets = SecretLeaseCreatedEvent.class.cast(leaseEvent).getSecrets();
                String username = secrets.get("username").toString();
                String password = secrets.get("password").toString();
                ds.setUsername(username);
                ds.setPassword(password);
            }
        });
        container.addRequestedSecret(secret);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [container=" + container + ", ds=" + ds + ", props=" + props + "]";
    }
}