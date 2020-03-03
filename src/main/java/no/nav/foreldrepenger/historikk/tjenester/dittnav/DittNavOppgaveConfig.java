package no.nav.foreldrepenger.historikk.tjenester.dittnav;

public class DittNavOppgaveTopics {
    private final String opprett;
    private final String avslutt;

    public DittNavOppgaveTopics(String opprett, String avslutt) {
        this.opprett = opprett;
        this.avslutt = avslutt;
    }

    public String getOpprett() {
        return opprett;
    }

    public String getAvslutt() {
        return avslutt;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[opprett=" + opprett + ", avslutt="
                + avslutt + "]";
    }
}
