package no.nav.foreldrepenger.historikk.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.ConfigurableEnvironment;

@Order(Ordered.LOWEST_PRECEDENCE)
public class ClusterAwareProfileSetter implements EnvironmentPostProcessor {

    private static final Logger LOG = LoggerFactory.getLogger(ClusterAwareProfileSetter.class);

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        LOG.info("XXXXXXXX");
    }

}
