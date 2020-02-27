package no.nav.foreldrepenger.historikk.config;

import static java.lang.System.getenv;
import static no.nav.foreldrepenger.boot.conditionals.EnvUtil.DEFAULT;
import static no.nav.foreldrepenger.boot.conditionals.EnvUtil.DEV_FSS;
import static no.nav.foreldrepenger.boot.conditionals.EnvUtil.DEV_GCP;
import static no.nav.foreldrepenger.boot.conditionals.EnvUtil.LOCAL;

import java.util.Optional;

public final class ClusterAwareSpringProfileResolver {

    private static final String NAIS_CLUSTER_NAME = "NAIS_CLUSTER_NAME";

    private ClusterAwareSpringProfileResolver() {

    }

    public static String[] profiles() {
        return Optional.ofNullable(clusterFra(getenv(NAIS_CLUSTER_NAME)))
                .map(c -> new String[] { c })
                .orElse(new String[0]);
    }

    private static String clusterFra(String cluster) {
        if (cluster == null) {
            return LOCAL;
        }
        if (cluster.equals(DEV_FSS)) {
            return DEV_FSS;
        }
        if (cluster.equals(DEV_GCP)) {
            return DEV_GCP;
        }
        return DEFAULT;
    }
}
