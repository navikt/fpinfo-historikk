package no.nav.foreldrepenger.historikk.config;

import static org.springframework.core.env.StandardEnvironment.SYSTEM_ENVIRONMENT_PROPERTY_SOURCE_NAME;

import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

@Order(Ordered.LOWEST_PRECEDENCE)
public class ClusterAwareProfileSetter implements EnvironmentPostProcessor {

    private static final Logger LOG = LoggerFactory.getLogger(ClusterAwareProfileSetter.class);

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        LOG.info("XXXXXXXX");
        Map<String, Object> prefixed = new LinkedHashMap<>();
        prefixed.put("jalla", "42");
        environment.getPropertySources()
                .addAfter(SYSTEM_ENVIRONMENT_PROPERTY_SOURCE_NAME, new MapPropertySource("prefixer", prefixed));
    }

}
