package no.nav.foreldrepenger.historikk.config;

import static no.nav.vault.jdbc.hikaricp.HikariCPVaultUtil.createHikariDataSourceWithVaultIntegration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.vault.config.databases.VaultDatabaseProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.vault.core.lease.SecretLeaseContainer;

import com.zaxxer.hikari.HikariDataSource;

import no.nav.vault.jdbc.hikaricp.VaultError;

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
        try {
            createHikariDataSourceWithVaultIntegration(ds, props.getBackend(), props.getRole());
        } catch (VaultError vaultError) {
            throw new RuntimeException("Vault feil ved opprettelse av databaseforbindelse", vaultError);
        }
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [container=" + container + ", ds=" + ds + ", props=" + props + "]";
    }
}
