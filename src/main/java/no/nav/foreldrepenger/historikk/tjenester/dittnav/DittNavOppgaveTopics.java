package no.nav.foreldrepenger.historikk.tjenester.dittnav;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DittNavOppgaveTopics {
    private final String opprettOppgaveTopic;
    private final String avsluttOppgaveTopic;

    public DittNavOppgaveTopics(
            @Value("${historikk.kafka.topics.oppgave.opprett}") String opprettOppgaveTopic,
            @Value("${historikk.kafka.topics.oppgave.done}") String avsluttOppgaveTopic) {
        this.opprettOppgaveTopic = opprettOppgaveTopic;
        this.avsluttOppgaveTopic = avsluttOppgaveTopic;
    }

    public String getOpprettOppgaveTopic() {
        return opprettOppgaveTopic;
    }

    public String getAvsluttOppgaveTopic() {
        return avsluttOppgaveTopic;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[opprettOppgaveTopic=" + opprettOppgaveTopic + ", avsluttOppgaveTopic="
                + avsluttOppgaveTopic + "]";
    }
}
