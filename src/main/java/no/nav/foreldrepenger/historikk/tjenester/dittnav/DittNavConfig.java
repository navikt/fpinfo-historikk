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
    private final Duration beskjedVarighet;
    private final Duration oppgaveVarighet;
    private Environment env;

    @ConstructorBinding
    public DittNavConfig(DittNavTopics topics,
                         @DefaultValue("90d") Duration beskjedVarighet,
                         @DefaultValue("28d") Duration oppgaveVarighet) {
        this.topics = topics;
        this.beskjedVarighet = beskjedVarighet;
        this.oppgaveVarighet = oppgaveVarighet;
    }

    Duration getBeskjedVarighet() {
        return beskjedVarighet;
    }

    Duration getOppgaveVarighet() {
        return oppgaveVarighet;
    }

    String getBeskjed() {
        return topics.beskjed();
    }

    String getDone() {
        return topics.done();
    }

    String getOppgave() {
        return topics.oppgave();
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
        return "DittNavConfig{" +
            "topics=" + topics +
            ", beskjedVarighet=" + beskjedVarighet +
            ", oppgaveVarighet=" + oppgaveVarighet +
            ", env=" + env +
            '}';
    }

    public static record DittNavTopics (String beskjed, String oppgave, String done) {}

}
