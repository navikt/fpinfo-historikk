package no.nav.foreldrepenger.historikk.config;

import static java.util.Collections.singletonList;
import static no.nav.foreldrepenger.historikk.util.EnvUtil.DEV;
import static no.nav.foreldrepenger.historikk.util.EnvUtil.LOCAL;
import static no.nav.foreldrepenger.historikk.util.EnvUtil.PROD;

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
        if (cluster.contains(DEV)) {
            return DEV;
        }
        if (cluster.contains(PROD)) {
            return PROD;
        }
        throw new IllegalArgumentException("Ukjent cluster " + cluster + " , kan ikke bestemme Spring-profil");
    }
}
