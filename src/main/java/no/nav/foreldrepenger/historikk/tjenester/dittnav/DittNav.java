package no.nav.foreldrepenger.historikk.tjenester.dittnav;

import no.nav.foreldrepenger.historikk.domain.Fødselsnummer;
import no.nav.foreldrepenger.historikk.tjenester.felles.HendelseType;

public interface DittNav {

    // void avsluttOppgave(Fødselsnummer fnr, String grupperingsId, String eventId);

    void opprettBeskjed(Fødselsnummer fnr, String grupperingsId, String eventId, String tekst, HendelseType h);

    // void opprettOppgave(Fødselsnummer fnr, String grupperingsId, String eventId,
    // String tekst, HendelseType h);

}
