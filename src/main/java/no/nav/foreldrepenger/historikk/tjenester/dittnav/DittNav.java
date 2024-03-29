package no.nav.foreldrepenger.historikk.tjenester.dittnav;

import no.nav.foreldrepenger.historikk.tjenester.innsending.InnsendingHendelse;

public interface DittNav {

    void opprettBeskjed(InnsendingHendelse h, String tekst);

    void opprettOppgave(InnsendingHendelse h, String tekst);

    void avsluttOppgave(String eventId);

}
