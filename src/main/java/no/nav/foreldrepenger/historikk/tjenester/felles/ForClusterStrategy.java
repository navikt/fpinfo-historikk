package no.nav.foreldrepenger.historikk.tjenester.felles;

import java.util.Arrays;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import no.finn.unleash.strategy.Strategy;
import no.nav.foreldrepenger.historikk.util.Cluster;

@Component
public class ForClusterStrategy implements Strategy {
    public static final String CLUSTER = "cluster";
    private static final Logger LOGGER = LoggerFactory.getLogger(ForClusterStrategy.class);

    private final Cluster currentCluster;

    public ForClusterStrategy(Environment env) {
        this.currentCluster = currentCluster(env);
    }

    private Cluster currentCluster(Environment env) {
        return Arrays.stream(Cluster.values())
                .filter(c -> c.isActive(env))
                .findFirst()
                .orElse(Cluster.LOCAL);
    }

    @Override
    public String getName() {
        return "forCluster";
    }

    @Override
    public boolean isEnabled(Map<String, String> parameters) {
        var clusters = parameters.get(CLUSTER);
        if (clusters != null) {
            return Arrays.asList(clusters.split(",", 0))
                    .stream().map(Cluster::valueOf)
                    .filter(currentCluster::equals)
                    .findFirst()
                    .isPresent();
        }
        return false;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[currentCluster=" + currentCluster + "]";
    }
}
