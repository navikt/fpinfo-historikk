package no.nav.foreldrepenger.historikk.util;

import static no.nav.foreldrepenger.historikk.config.Constants.ISSUER;
import static no.nav.foreldrepenger.historikk.config.Constants.TOKENX;

import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;

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

    public boolean erAutentisert() {
        return getSubject() != null;
    }

    public Date getExpiryDate() {
        return Optional.ofNullable(claimSet())
                .map(JwtTokenClaims::getExpirationTime)
                .orElse(null);
    }

    public String getSubject() {
        return Optional.ofNullable(claimSet())
                .map(JwtTokenClaims::getSubject)
                .orElse(null);
    }

    public String autentisertBruker() {
        return Optional.ofNullable(getSubject())
                .orElseThrow(unauthenticated("Fant ikke subject"));
    }

    public Fødselsnummer autentisertFNR() {
        return Fødselsnummer.valueOf(autentisertBruker());
    }

    private static Supplier<? extends JwtTokenValidatorException> unauthenticated(String msg) {
        return () -> new JwtTokenValidatorException(msg);
    }

    private JwtTokenClaims claimSet() {
        return Stream.of(ISSUER, TOKENX)
                .map(this::claimSet)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }

    private JwtTokenClaims claimSet(String issuer) {
        return Optional.ofNullable(context())
                .map(s -> s.getClaims(issuer))
                .orElse(null);
    }

    private TokenValidationContext context() {
        return Optional.ofNullable(ctxHolder.getTokenValidationContext())
                .orElse(null);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [ctxHolder=" + ctxHolder + "]";
    }
}