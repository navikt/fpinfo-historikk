package no.nav.foreldrepenger.historikk.tjenester.dittnav;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConfigurationProperties(prefix = "historikk.dittnav")
public class DittNavConfig {
    private final DittNavTopics topics;
    private final boolean enabled;

    @ConstructorBinding
    public DittNavConfig(DittNavTopics topics, boolean enabled) {
        this.topics = topics;
        this.enabled = enabled;
    }

    public DittNavTopics getTopics() {
        return topics;
    }

    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[topics=" + topics + ", enabled=" + enabled + "]";
    }

}
