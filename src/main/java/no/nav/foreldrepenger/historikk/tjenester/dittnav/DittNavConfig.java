package no.nav.foreldrepenger.historikk.tjenester.dittnav;

import java.time.Duration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.boot.context.properties.bind.DefaultValue;

@ConfigurationProperties(prefix = "historikk.dittnav")
public class DittNavConfig {
    private final DittNavTopics topics;
    private final boolean enabled;
    private final Duration varighet;

    @ConstructorBinding
    public DittNavConfig(DittNavTopics topics, boolean enabled, @DefaultValue("90d") Duration varighet) {
        this.topics = topics;
        this.enabled = enabled;
        this.varighet = varighet;

    }

    public Duration getVarighet() {
        return varighet;
    }

    public DittNavTopics getTopics() {
        return topics;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public String getBeskjed() {
        return topics.getBeskjed();
    }

    public String getDone() {
        return topics.getDone();
    }

    public String getOppgave() {
        return topics.getOppgave();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [topics=" + topics + ", enabled=" + enabled + ", varighet=" + varighet + "]";
    }

}
