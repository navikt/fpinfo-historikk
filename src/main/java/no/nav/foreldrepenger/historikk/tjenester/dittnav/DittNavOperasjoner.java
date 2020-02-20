package no.nav.foreldrepenger.historikk.tjenester.dittnav;

import no.nav.foreldrepenger.historikk.tjenester.innsending.InnsendingHendelse;

public interface DittNavOperasjoner {

    void opprettBeskjed(InnsendingHendelse h);

    void opprettBeskjed(BeskjedDTO dto);

    void avsluttOppgave(DoneDTO dto);

    void opprettOppgave(OppgaveDTO dto);

}
