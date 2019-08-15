package no.nav.foreldrepenger.historikk.config;

import static no.nav.foreldrepenger.historikk.util.EnvUtil.DEFAULT;
import static no.nav.foreldrepenger.historikk.util.EnvUtil.DEV;
import static no.nav.foreldrepenger.historikk.util.EnvUtil.LOCAL;

import org.springframework.boot.logging.DeferredLog;

public class ClusterAwareSpringProfileResolver {

    private static final String NAIS_CLUSTER_NAME = "NAIS_CLUSTER_NAME";
    private static final DeferredLog LOG = new DeferredLog();

    public String getProfile() {
        String cluster = clusterFra(System.getenv(NAIS_CLUSTER_NAME));
        LOG.info("Vi er i cluster " + cluster);
        return cluster;
    }

    private static String clusterFra(String cluster) {
        if (cluster == null) {
            return LOCAL;
        }
        if (cluster.contains(DEV)) {
            return DEV;
        }

        return DEFAULT;
    }

}
