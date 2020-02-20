package no.nav.foreldrepenger.historikk.tjenester.dittnav;

import no.nav.foreldrepenger.historikk.tjenester.innsending.InnsendingHendelse;
import no.nav.foreldrepenger.historikk.tjenester.minidialog.MinidialogHendelse;

public interface DittNavOperasjoner {

    void opprettBeskjed(InnsendingHendelse h);

    void opprettBeskjed(BeskjedDTO dto);

    void avsluttOppgave(DoneDTO dto);

    void opprettOppgave(OppgaveDTO dto);

    void opprettOppgave(MinidialogHendelse h);

    void avsluttOppgave(MinidialogHendelse h);

}
