
package no.nav.foreldrepenger.historikk.error;

import static java.lang.String.format;

public class UnexpectedInputException extends RuntimeException {

    public UnexpectedInputException(String msg, Throwable e, Object... args) {
        super(format(msg, args), e);
    }

}
