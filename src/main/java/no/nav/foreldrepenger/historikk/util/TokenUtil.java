package no.nav.foreldrepenger.historikk.util;

import static no.nav.foreldrepenger.historikk.config.Constants.TOKENX;

import java.util.Date;
import java.util.Optional;
import java.util.function.Supplier;

import org.springframework.stereotype.Component;

import no.nav.foreldrepenger.historikk.domain.Fødselsnummer;
import no.nav.security.token.support.core.context.TokenValidationContext;
import no.nav.security.token.support.core.context.TokenValidationContextHolder;
import no.nav.security.token.support.core.exceptions.JwtTokenValidatorException;
import no.nav.security.token.support.core.jwt.JwtTokenClaims;

@Component
public class TokenUtil {
    private final TokenValidationContextHolder ctxHolder;

    public TokenUtil(TokenValidationContextHolder ctxHolder) {
        this.ctxHolder = ctxHolder;
    }

    public Date getExpiryDate() {
        return claimSet()
                .map(JwtTokenClaims::getExpirationTime)
                .orElse(null);
    }

    public Fødselsnummer fødselsnummerFraToken() {
        return authenticatedFødselsnummer().orElseThrow(unauthenticated("Fant ikke subject"));
    }

    public Optional<Fødselsnummer> authenticatedFødselsnummer() {
        return claimSet()
            .map(TokenUtil::getIdentFromPidOrSub)
            .map(Fødselsnummer::valueOf);
    }

    private static String getIdentFromPidOrSub(JwtTokenClaims claims) {
        return Optional.ofNullable(claims.getStringClaim("pid"))
            .orElseGet(claims::getSubject);
    }

    private Optional<JwtTokenClaims> claimSet() {
        return Optional.ofNullable(context())
            .map(s -> s.getClaims(TOKENX));
    }

    private TokenValidationContext context() {
        return ctxHolder.getTokenValidationContext();
    }

    private static Supplier<? extends JwtTokenValidatorException> unauthenticated(String msg) {
        return () -> new JwtTokenValidatorException(msg);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [ctxHolder=" + ctxHolder + "]";
    }
}
