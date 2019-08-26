package no.nav.foreldrepenger.historikk.config;

import static java.util.Collections.singletonList;
import static no.nav.foreldrepenger.historikk.util.EnvUtil.DEFAULT;
import static no.nav.foreldrepenger.historikk.util.EnvUtil.DEV;
import static no.nav.foreldrepenger.historikk.util.EnvUtil.LOCAL;

public final class ClusterAwareSpringProfileResolver {

    private static final String NAIS_CLUSTER_NAME = "NAIS_CLUSTER_NAME";

    private ClusterAwareSpringProfileResolver() {

    }

    public static String[] profiles() {
        return singletonList(clusterFra(System.getenv(NAIS_CLUSTER_NAME)))
                .stream()
                .toArray(String[]::new);
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
