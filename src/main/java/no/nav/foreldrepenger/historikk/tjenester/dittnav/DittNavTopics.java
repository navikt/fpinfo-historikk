package no.nav.foreldrepenger.historikk.tjenester.dittnav;

public class DittNavTopics {
    private final DittNavOppgaveConfig oppgave;
    private final String beskjed;

    public DittNavTopics(DittNavOppgaveConfig oppgave, String beskjed) {
        this.oppgave = oppgave;
        this.beskjed = beskjed;
    }

    public DittNavOppgaveConfig getOppgave() {
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
