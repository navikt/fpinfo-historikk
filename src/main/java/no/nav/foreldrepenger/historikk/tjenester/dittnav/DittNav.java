package no.nav.foreldrepenger.historikk.tjenester.dittnav;

import no.nav.foreldrepenger.historikk.domain.Fødselsnummer;

public interface DittNav {

    void avsluttOppgave(Fødselsnummer fnr, String grupperingsId, String eventId);

    void avsluttBeskjed(Fødselsnummer fnr, String grupperingsId, String eventId);

    void opprettBeskjed(Fødselsnummer fnr, String grupperingsId, String eventId,
            String tekst, String url, String saksnr);

    void opprettOppgave(Fødselsnummer fnr, String grupperingsId, String eventId,
            String tekst, String url, String saksnr);

    boolean oppgaveOpprettet(String saksnr);

    void registrerOppgaveOpprettet(Fødselsnummer fnr, String saksnummer, String referanseId);

}
