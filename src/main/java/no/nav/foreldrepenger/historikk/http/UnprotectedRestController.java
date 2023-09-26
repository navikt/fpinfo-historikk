package no.nav.foreldrepenger.historikk.http;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.springframework.core.annotation.AliasFor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import no.nav.security.token.support.core.api.Unprotected;

@RestController
@RequestMapping
@Unprotected
@Documented
@Target(TYPE)
@Retention(RUNTIME)
public @interface UnprotectedRestController {
    @AliasFor(annotation = RequestMapping.class, attribute = "value")
    String[] value() default {};

    @AliasFor(annotation = RequestMapping.class, attribute = "produces")
    String[] produces() default { APPLICATION_JSON_VALUE };

}
