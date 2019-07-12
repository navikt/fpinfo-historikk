package no.nav.foreldrepenger.historikk.config;

import static org.springframework.core.Ordered.LOWEST_PRECEDENCE;
import static org.springframework.core.env.StandardEnvironment.SYSTEM_ENVIRONMENT_PROPERTY_SOURCE_NAME;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.boot.logging.DeferredLog;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.stereotype.Component;

@Order(LOWEST_PRECEDENCE)
@Component
public class ClusterAwareProfileSetter implements ApplicationListener<ApplicationEvent>, EnvironmentPostProcessor {

    private static final DeferredLog LOG = new DeferredLog();

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        LOG.info("XXXXXXXX");
        Map<String, Object> prefixed = new LinkedHashMap<>();
        prefixed.put("jalla", "42");
        environment.getPropertySources()
                .addAfter(SYSTEM_ENVIRONMENT_PROPERTY_SOURCE_NAME, new MapPropertySource("prefixer", prefixed));
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        LOG.replayTo(ClusterAwareProfileSetter.class);
    }

}
