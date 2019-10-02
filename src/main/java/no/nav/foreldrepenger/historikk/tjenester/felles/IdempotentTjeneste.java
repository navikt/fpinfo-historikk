package no.nav.foreldrepenger.historikk.tjenester.felles;

public interface IdempotentTjeneste<T extends Hendelse> {

    boolean erAlleredeLagret(String referanseId);

    default boolean skalLagre(T hendelse) {
        String referanseId = hendelse.getReferanseId();
        return referanseId == null || erAlleredeLagret(referanseId);
    }

    void lagre(T hendelse);

}
