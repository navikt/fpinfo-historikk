package no.nav.foreldrepenger.historikk.error;

public class UnexpectedResponseException extends RuntimeException {

    public UnexpectedResponseException(String msg) {
        super(msg);
    }
}
