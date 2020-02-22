package no.nav.foreldrepenger.historikk.tjenester.dittnav;

import no.nav.foreldrepenger.historikk.domain.Fødselsnummer;

public interface DittNavOperasjoner {

    void avsluttOppgave(Fødselsnummer fnr, String grupperingsId, String eventId);

    void opprettBeskjed(Fødselsnummer fnr, String grupperingsId, String tekst, String url, String eventId);

    void opprettOppgave(Fødselsnummer fnr, String grupperingsId, String tekst, String url, String eventId);

}
