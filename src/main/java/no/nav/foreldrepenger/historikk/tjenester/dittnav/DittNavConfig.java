package no.nav.foreldrepenger.historikk.tjenester.dittnav;

import static no.nav.foreldrepenger.boot.conditionals.EnvUtil.isDev;

import java.net.URI;
import java.time.Duration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

@ConfigurationProperties(prefix = "historikk.dittnav")
public class DittNavConfig implements EnvironmentAware {
    private static final URI FP_DEV = URI.create("https://foreldrepenger.dev.nav.no/");
    private static final URI FP_PROD = URI.create("https://foreldrepenger.nav.no");
    private final DittNavTopics topics;
    private final boolean enabled;
    private final Duration varighet;
    private Environment env;

    @ConstructorBinding
    public DittNavConfig(DittNavTopics topics, boolean enabled, @DefaultValue("90d") Duration varighet) {
        this.topics = topics;
        this.enabled = enabled;
        this.varighet = varighet;
    }

    Duration getVarighet() {
        return varighet;
    }

    DittNavTopics getTopics() {
        return topics;
    }

    boolean isEnabled() {
        return enabled;
    }

    String getBeskjed() {
        return topics.getBeskjed();
    }

    String getDone() {
        return topics.getDone();
    }

    String getOppgave() {
        return topics.getOppgave();
    }

    @Override
    public void setEnvironment(Environment env) {
        this.env = env;
    }

    public URI uri() {
        return isDev(env) ? FP_DEV : FP_PROD;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [topics=" + topics + ", enabled=" + enabled + ", varighet=" + varighet + ", env=" + env + ", uri="
                + uri() + "]";
    }

}
