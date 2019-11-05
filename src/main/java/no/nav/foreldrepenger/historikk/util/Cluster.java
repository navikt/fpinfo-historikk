package no.nav.foreldrepenger.historikk.util;

import static no.nav.foreldrepenger.historikk.config.Constants.NAIS_CLUSTER_NAME;

import java.util.Optional;

import org.springframework.core.env.Environment;

public enum Cluster {
    LOCAL(EnvUtil.LOCAL),
    DEV_FSS(EnvUtil.DEV_FSS),
    PROD_FSS(EnvUtil.PROD_FSS);

    private final String clusterName;

    Cluster(String clusterName) {
        this.clusterName = clusterName;
    }

    public String clusterName() {
        return clusterName;
    }

    public boolean isActive(Environment env) {
        return Optional.ofNullable(env.getProperty(NAIS_CLUSTER_NAME))
                .filter(clusterName::equals)
                .isPresent();
    }
}
