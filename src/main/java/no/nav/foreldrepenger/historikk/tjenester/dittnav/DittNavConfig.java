package no.nav.foreldrepenger.historikk.tjenester.dittnav;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConfigurationProperties(prefix = "historikk.kafka.topics")
public class DittNavTopics {
    private final DittNavOppgaveTopics oppgave;
    private final String beskjed;

    @ConstructorBinding
    public DittNavTopics(DittNavOppgaveTopics oppgave, String beskjed) {
        this.oppgave = oppgave;
        this.beskjed = beskjed;
    }

    public DittNavOppgaveTopics getOppgaveTopics() {
        return oppgave;
    }

    public String getBeskjed() {
        return beskjed;
    }

    public String getAvslutt() {
        return oppgave.getAvslutt();
    }

    public String getOpprett() {
        return oppgave.getOpprett();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[oppgave=" + oppgave + ", beskjed=" + beskjed + "]";
    }

}
