package no.nav.foreldrepenger.historikk.tjenester.dittnav;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DittNavTopics {
    private final DittNavOppgaveTopics oppgaveTopics;
    private final String beskjedTopic;

    public DittNavTopics(DittNavOppgaveTopics oppgaveTopics,
            @Value("${historikk.kafka.topics.beskjed}") String beskjedTopic) {
        this.oppgaveTopics = oppgaveTopics;
        this.beskjedTopic = beskjedTopic;
    }

    public DittNavOppgaveTopics getOppgaveTopics() {
        return oppgaveTopics;
    }

    public String getBeskjedTopic() {
        return beskjedTopic;
    }

    public String getAvsluttOppgaveTopic() {
        return oppgaveTopics.getAvsluttOppgaveTopic();
    }

    public String getOpprettOppgaveTopic() {
        return oppgaveTopics.getOpprettOppgaveTopic();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[oppgaveTopics=" + oppgaveTopics + ", beskjedTopic=" + beskjedTopic + "]";
    }

}
