package no.nav.foreldrepenger.historikk.config;

import static java.util.Collections.singletonList;
import static no.nav.foreldrepenger.historikk.util.EnvUtil.DEVFSS;
import static no.nav.foreldrepenger.historikk.util.EnvUtil.LOCAL;
import static no.nav.foreldrepenger.historikk.util.EnvUtil.PRODFSS;

public final class ClusterAwareSpringProfileResolver {

    private static final String NAIS_CLUSTER_NAME = "NAIS_CLUSTER_NAME";

    private ClusterAwareSpringProfileResolver() {

    }

    public static String[] profiles() {
        String cluster = clusterFra(System.getenv(NAIS_CLUSTER_NAME));
        if (cluster != null) {
            return singletonList(cluster)
                    .stream()
                    .toArray(String[]::new);
        }
        return new String[0];
    }

    private static String clusterFra(String cluster) {
        if (cluster == null) {
            return LOCAL;
        }
        if (cluster.equals(DEVFSS)) {
            return DEVFSS;
        }
        if (cluster.equals(PRODFSS)) {
            return PRODFSS;
        }
        throw new IllegalArgumentException("Ukjent cluster " + cluster + " , kan ikke bestemme Spring-profil");
    }
}
