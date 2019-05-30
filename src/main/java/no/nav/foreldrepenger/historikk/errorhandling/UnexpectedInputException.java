
package no.nav.foreldrepenger.historikk.errorhandling;

import static java.lang.String.format;

public class UnexpectedInputException extends RuntimeException {
    public UnexpectedInputException(String msg) {
        this(msg, (Throwable) null);
    }

    public UnexpectedInputException(String msg, Object... args) {
        this(msg, null, args);
    }

    public UnexpectedInputException(String msg, Throwable e, Object... args) {
        this(format(msg, args), e);
    }

    public UnexpectedInputException(String msg, Throwable t) {
        super(msg, t);
    }
}
