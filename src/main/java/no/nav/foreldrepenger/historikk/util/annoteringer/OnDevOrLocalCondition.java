package no.nav.foreldrepenger.historikk.util.annoteringer;

import static no.nav.foreldrepenger.historikk.util.Cluster.DEV_FSS;
import static no.nav.foreldrepenger.historikk.util.Cluster.LOCAL;

import org.springframework.core.type.AnnotatedTypeMetadata;

import no.nav.foreldrepenger.historikk.util.Cluster;

public class OnDevOrLocalCondition extends OnClusterCondition {

    @Override
    protected Cluster[] clusters(AnnotatedTypeMetadata metadata) {
        return new Cluster[] { LOCAL, DEV_FSS };
    }

}
