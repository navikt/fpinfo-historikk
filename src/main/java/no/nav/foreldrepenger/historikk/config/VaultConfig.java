package no.nav.foreldrepenger.historikk.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix = "vault")
public class VaultConfig {
    private boolean enabled = true;
    private String databaseBackend;
    private String databaseRole;
    private String databaseAdminRole;
}
