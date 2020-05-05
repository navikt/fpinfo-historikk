package no.nav.foreldrepenger.historikk.tjenester.dittnav;

public class DittNavTopics {
    private final String oppgave;
    private final String beskjed;
    private final String done;

    public DittNavTopics(String oppgave, String beskjed, String done) {
        this.oppgave = oppgave;
        this.beskjed = beskjed;
        this.done = done;
    }

    public String getBeskjed() {
        return beskjed;
    }

    public String getDone() {
        return done;
    }

    public String getOppgave() {
        return oppgave;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[oppgave=" + oppgave + ", beskjed=" + beskjed + ", done=" + done + "]";
    }

}
