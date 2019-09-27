package no.nav.foreldrepenger.historikk.tjenester.felles;

import no.nav.foreldrepenger.historikk.tjenester.felles.Hendelse;

public interface IdempotentTjeneste {

    public boolean erAlleredeLagret(String referanseId);

    default boolean skalLagre(Hendelse hendelse) {
        String referanseId = hendelse.getReferanseId();
        return referanseId == null || erAlleredeLagret(referanseId);
    }
}
