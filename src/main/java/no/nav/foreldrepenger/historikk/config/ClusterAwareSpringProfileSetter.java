package no.nav.foreldrepenger.historikk.config;

import static no.nav.foreldrepenger.historikk.util.EnvUtil.DEFAULT;
import static no.nav.foreldrepenger.historikk.util.EnvUtil.DEV;
import static no.nav.foreldrepenger.historikk.util.EnvUtil.LOCAL;
import static org.springframework.core.Ordered.LOWEST_PRECEDENCE;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.boot.logging.DeferredLog;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.stereotype.Component;

@Order(LOWEST_PRECEDENCE)
@Component
public class ClusterAwareSpringProfileSetter
        implements ApplicationListener<ApplicationEvent>, EnvironmentPostProcessor {

    private static final String NAIS_CLUSTER_NAME = "nais.cluster.name";
    private static final DeferredLog LOG = new DeferredLog();

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment env, SpringApplication app) {
        app.setLogStartupInfo(true);
        String cluster = clusterFra(env.getProperty(NAIS_CLUSTER_NAME, LOCAL));
        LOG.info("Vi er i cluster " + cluster);
        List<String> aktive = new ArrayList<>(Arrays.asList(env.getActiveProfiles()));
        aktive.add(cluster);
        env.setActiveProfiles(aktive.stream().toArray(String[]::new));
    }

    private static String clusterFra(String cluster) {
        if (cluster.contains(DEV)) {
            return DEV;
        }
        if (cluster.contains(LOCAL)) {
            return LOCAL;
        }
        return DEFAULT;

    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        LOG.replayTo(ClusterAwareSpringProfileSetter.class);
    }

}
