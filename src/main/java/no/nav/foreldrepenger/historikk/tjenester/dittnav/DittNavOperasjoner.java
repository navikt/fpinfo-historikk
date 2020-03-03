package no.nav.foreldrepenger.historikk.tjenester.dittnav;

import no.nav.foreldrepenger.historikk.domain.Fødselsnummer;
import no.nav.foreldrepenger.historikk.tjenester.felles.HendelseType;

public interface DittNavOperasjoner {

    void avsluttOppgave(Fødselsnummer fnr, String grupperingsId, String eventId);

    void opprettBeskjed(Fødselsnummer fnr, String grupperingsId, String tekst, HendelseType h, String eventId);

    void opprettOppgave(Fødselsnummer fnr, String grupperingsId, String tekst, HendelseType h, String eventId);

}
