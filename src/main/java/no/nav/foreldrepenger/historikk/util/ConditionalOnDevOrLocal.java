package no.nav.foreldrepenger.historikk.util;

import static no.nav.foreldrepenger.historikk.util.Cluster.DEV_FSS;
import static no.nav.foreldrepenger.historikk.util.Cluster.DEV_GCP;
import static no.nav.foreldrepenger.historikk.util.Cluster.LOCAL;

@ConditionalOnClusters(clusters = { DEV_GCP, DEV_FSS, LOCAL })
public @interface ConditionalOnDevOrLocal {

}
