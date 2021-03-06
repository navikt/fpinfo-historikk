package no.nav.foreldrepenger.historikk.tjenester.dittnav;

import no.nav.foreldrepenger.historikk.domain.Fødselsnummer;
import no.nav.foreldrepenger.historikk.tjenester.innsending.InnsendingHendelse;

public interface DittNav {

    void avsluttOppgave(Fødselsnummer fnr, String grupperingsId, String eventId);

    void avsluttBeskjed(Fødselsnummer fnr, String grupperingsId, String eventId);

    void opprettBeskjed(InnsendingHendelse h, String tekst);

    void opprettOppgave(InnsendingHendelse h, String tekst);

}
