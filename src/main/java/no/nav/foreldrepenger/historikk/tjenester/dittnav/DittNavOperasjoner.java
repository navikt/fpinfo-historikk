package no.nav.foreldrepenger.historikk.tjenester.dittnav;

import no.nav.foreldrepenger.historikk.domain.Fødselsnummer;
import no.nav.foreldrepenger.historikk.tjenester.innsending.InnsendingHendelse;
import no.nav.foreldrepenger.historikk.tjenester.minidialog.MinidialogHendelse;

public interface DittNavOperasjoner {

    void opprettBeskjed(String fnr, String grupperingsId, String tekst, String url);

    void opprettBeskjed(InnsendingHendelse h);

    void opprettOppgave(MinidialogHendelse h);

    void avsluttOppgave(Fødselsnummer fnr, String grupperingsId, String eventId);

}
