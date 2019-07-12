package no.nav.foreldrepenger.historikk.config;

import static org.springframework.core.Ordered.LOWEST_PRECEDENCE;

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
public class ClusterAwareProfileSetter implements ApplicationListener<ApplicationEvent>, EnvironmentPostProcessor {

    private static final DeferredLog LOG = new DeferredLog();

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        String cluster = environment.getProperty("nais.cluster.name", "jalla");
        LOG.info("XXXXXXXX " + cluster);
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        LOG.replayTo(ClusterAwareProfileSetter.class);
    }

}
