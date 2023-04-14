package no.nav.foreldrepenger.historikk.tjenester.dittnav;

import no.nav.foreldrepenger.historikk.http.AbstractConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.net.URI;
import java.time.Duration;

@ConfigurationProperties(prefix = "historikk.dittnav")
public class DittNavConfig extends AbstractConfig {
    private static final String DEFAULT_BASE_PATH = "https://foreldrepenger.nav.no";

    private final DittNavTopics topics;
    private final Duration beskjedVarighet;
    private final Duration oppgaveVarighet;

    public DittNavConfig(@DefaultValue(DEFAULT_BASE_PATH) URI baseUri,
                         @DefaultValue("true") boolean enabled,
                         @DefaultValue("90d") Duration beskjedVarighet,
                         @DefaultValue("28d") Duration oppgaveVarighet,
                         DittNavTopics topics) {
        super(baseUri, "", enabled);
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
    public URI pingEndpoint() {
        throw new UnsupportedOperationException("Før bruk må du sette ping path til riktig verdi!");
    }

    @Override
    public String toString() {
        return "DittNavConfig{" +
            "topics=" + topics +
            ", beskjedVarighet=" + beskjedVarighet +
            ", oppgaveVarighet=" + oppgaveVarighet +
            '}';
    }

    public static record DittNavTopics (String beskjed, String oppgave, String done) {}

}
