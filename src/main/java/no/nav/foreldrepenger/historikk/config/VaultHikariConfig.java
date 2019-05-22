package no.nav.foreldrepenger.historikk.config;

import static no.nav.foreldrepenger.historikk.util.EnvUtil.CONFIDENTIAL;
import static org.springframework.vault.core.lease.domain.RequestedSecret.rotating;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.vault.config.databases.VaultDatabaseProperties;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.vault.core.lease.LeaseEndpoints;
import org.springframework.vault.core.lease.SecretLeaseContainer;
import org.springframework.vault.core.lease.domain.RequestedSecret;
import org.springframework.vault.core.lease.event.SecretLeaseCreatedEvent;

import com.zaxxer.hikari.HikariDataSource;

@Configuration
@ConditionalOnProperty(value = "vault.enabled", matchIfMissing = true)
public class VaultHikariConfig implements InitializingBean, EnvironmentAware {
    private static final Logger LOGGER = LoggerFactory.getLogger(VaultHikariConfig.class.getName());
    private final SecretLeaseContainer container;
    private final HikariDataSource ds;
    private final VaultDatabaseProperties props;

    public VaultHikariConfig(SecretLeaseContainer container, HikariDataSource ds, VaultDatabaseProperties props) {
        this.container = container;
        this.ds = ds;
        this.props = props;
    }

    @Override
    public void afterPropertiesSet() {
        container.setLeaseEndpoints(LeaseEndpoints.SysLeases);
        RequestedSecret secret = rotating(props.getBackend() + "/creds/" + props.getRole());
        container.addLeaseListener(leaseEvent -> {
            if (leaseEvent.getSource() == secret && leaseEvent instanceof SecretLeaseCreatedEvent) {
                LOGGER.info("Rotating creds for path: {}", leaseEvent.getSource().getPath());
                Map<String, Object> secrets = SecretLeaseCreatedEvent.class.cast(leaseEvent).getSecrets();
                String username = secrets.get("username").toString();
                String password = secrets.get("password").toString();
                LOGGER.info(CONFIDENTIAL, "Credentials {} {} {}", ds.getJdbcUrl(), username, password);
                ds.setUsername(username);
                ds.setPassword(password);
                ds.getHikariConfigMXBean().setUsername(username);
                ds.getHikariConfigMXBean().setPassword(password);
            }
        });
        container.addRequestedSecret(secret);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [container=" + container + ", ds=" + ds + ", props=" + props + "]";
    }

    @Override
    public void setEnvironment(Environment environment) {
        LOGGER.info("XXX " + environment.getProperty("kafka.password"));
    }
}
