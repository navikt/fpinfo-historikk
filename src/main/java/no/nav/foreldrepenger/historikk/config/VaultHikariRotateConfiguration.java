package no.nav.foreldrepenger.historikk.config;

import static org.springframework.vault.core.lease.domain.RequestedSecret.renewable;
import static org.springframework.vault.core.lease.domain.RequestedSecret.rotating;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.vault.config.databases.VaultDatabaseProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.vault.core.lease.SecretLeaseContainer;
import org.springframework.vault.core.lease.event.SecretLeaseCreatedEvent;

import com.zaxxer.hikari.HikariDataSource;

@Configuration
@ConditionalOnProperty(value = "spring.cloud.vault.database.enabled")
class VaultHikariRotateConfiguration implements InitializingBean {
    private static final Logger LOG = LoggerFactory.getLogger(VaultHikariRotateConfiguration.class);
    private final ConfigurableApplicationContext applicationContext;
    private final SecretLeaseContainer container;
    private final VaultDatabaseProperties props;
    private final HikariDataSource ds;


    public VaultHikariRotateConfiguration(ConfigurableApplicationContext applicationContext,
                                          SecretLeaseContainer container,
                                          VaultDatabaseProperties props,
                                          HikariDataSource ds) {
        this.applicationContext = applicationContext;
        this.container = container;
        this.props = props;
        this.ds = ds;
    }

    @Override // Roterer brukernavn og passord for postgres når lease er gått ut
    public void afterPropertiesSet() {
        var pathTilVaultDBSecrets = props.getBackend() + "/creds/" + props.getRole();
        container.addLeaseListener(leaseEvent -> {
            if (leaseEvent.getSource().equals(renewable(pathTilVaultDBSecrets))) { // Når lease for DB credetial er utgått
                container.requestRotatingSecret(pathTilVaultDBSecrets);
            }
            if (leaseEvent.getSource().equals(rotating(pathTilVaultDBSecrets)) && leaseEvent instanceof SecretLeaseCreatedEvent event) {
                LOG.info("Roterer brukernavn/passord for {}", pathTilVaultDBSecrets);
                var secrets = event.getSecrets();
                if (secrets.isEmpty()) {
                    LOG.warn("Klarte ikke å rotere brukernavn/passord for : {}. Restarter applikasjonen!", pathTilVaultDBSecrets);
                    applicationContext.close();
                }
                var username = get("username", secrets);
                var password = get("password", secrets);
                oppdaterDBProperties(username, password);
                oppdaterDataSource(username, password);
                LOG.info("Rotasjon av brukernavn/passord av {} var vellykket!", pathTilVaultDBSecrets);
            }
        });
    }

    private void oppdaterDBProperties(String username, String password) {
        System.setProperty("spring.datasource.username", username);
        System.setProperty("spring.datasource.password", password);
    }

    private void oppdaterDataSource(String username, String password) {
        ds.getHikariConfigMXBean().setUsername(username);
        ds.getHikariConfigMXBean().setPassword(password);
        ds.setUsername(username);
        ds.setPassword(password);
        ds.getHikariPoolMXBean().softEvictConnections();
    }

    private static String get(String key, Map<String, Object> secrets) {
        return secrets.get(key).toString();
    }

}
