package no.nav.foreldrepenger.historikk.errorhandling;

public class UnexpectedResponseException extends RuntimeException {

    public UnexpectedResponseException(String msg) {
        super(msg);
    }
}
