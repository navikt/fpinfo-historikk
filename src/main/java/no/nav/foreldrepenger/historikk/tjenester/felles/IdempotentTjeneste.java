package no.nav.foreldrepenger.historikk.tjenester.felles;

import no.nav.foreldrepenger.historikk.domain.Fødselsnummer;

public interface IdempotentTjeneste<T extends Hendelse> {

    boolean erAlleredeLagret(String referanseId);

    void lagre(T hendelse, Fødselsnummer fnr);

}
