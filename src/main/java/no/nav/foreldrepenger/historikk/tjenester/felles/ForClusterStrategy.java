package no.nav.foreldrepenger.historikk.tjenester.felles;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
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

    private final Environment env;

    public ForClusterStrategy(Environment env) {
        this.env = env;
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
        String cluster = parameters.get(CLUSTER);
        return Arrays.stream(Optional.ofNullable(cluster)
                .map(p -> p.split(","))
                .orElse(new String[0]))
                .filter(Objects::nonNull)
                .map(Cluster::valueOf)
                .filter(c -> c.isActive(env))
                .findAny()
                .isPresent();
    }

}
