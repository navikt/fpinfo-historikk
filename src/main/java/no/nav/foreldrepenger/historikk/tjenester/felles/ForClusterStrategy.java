package no.nav.foreldrepenger.historikk.tjenester.felles;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import no.finn.unleash.UnleashContext;
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
        return false;
    }

    @Override
    public boolean isEnabled(Map<String, String> parameters, UnleashContext unleashContext) {
        return Optional.ofNullable(parameters)
                .map(par -> par.get(CLUSTER))
                .filter(s -> !s.isEmpty())
                .map(clusters -> clusters.split(","))
                .map(Arrays::stream)
                .filter(c -> currentCluster.clusterName().equals(c))
                .isPresent();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[currentCluster=" + currentCluster + "]";
    }
}
