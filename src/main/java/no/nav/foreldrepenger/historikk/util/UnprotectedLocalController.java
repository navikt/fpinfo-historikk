package no.nav.foreldrepenger.historikk.util;

import static no.nav.foreldrepenger.historikk.util.EnvUtil.DEV;
import static no.nav.foreldrepenger.historikk.util.EnvUtil.LOCAL;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.RestController;

import no.nav.security.oidc.api.Unprotected;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@RestController
@Profile({ LOCAL, DEV })
@Unprotected
public @interface UnprotectedLocalController {

}
