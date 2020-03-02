package no.nav.foreldrepenger.historikk.tjenester.felles;

public interface IdempotentTjeneste<T extends Hendelse> {

    boolean erAlleredeLagret(String referanseId);

    boolean opprettOppgave(T hendelse);

}
