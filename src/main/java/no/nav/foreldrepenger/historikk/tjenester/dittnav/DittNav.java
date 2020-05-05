package no.nav.foreldrepenger.historikk.tjenester.dittnav;

import no.nav.foreldrepenger.historikk.domain.Fødselsnummer;

public interface DittNav {

    void avslutt(Fødselsnummer fnr, String grupperingsId, String eventId);

    void opprettBeskjed(Fødselsnummer fnr, String grupperingsId, String eventId,
            String tekst, String url);

    void opprettOppgave(Fødselsnummer fnr, String grupperingsId, String eventId,
            String tekst, String url);

}
