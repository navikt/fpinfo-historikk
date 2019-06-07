package no.nav.foreldrepenger.historikk.meldinger;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import no.nav.foreldrepenger.historikk.util.TokenUtil;

@Aspect
@Component
public class AuthorizationAspect {

    private final TokenUtil tokenUtil;

    public AuthorizationAspect(TokenUtil tokenUtil) {
        this.tokenUtil = tokenUtil;
    }

    private static final Logger LOG = LoggerFactory.getLogger(AuthorizationAspect.class);

    @Before("@annotation(AuthorizeAktørId)")
    public void logBeforeAllMethods(JoinPoint joinPoint) {
        LOG.info("Kaller {}", joinPoint.getSignature());
    }
}
