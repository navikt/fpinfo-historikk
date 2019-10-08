package no.nav.foreldrepenger.historikk.tjenester.felles;

public interface IdempotentTjeneste<T extends Hendelse> {

    boolean erAlleredeLagret(String referanseId);

    void aktiver(T hendelse);

}
