package no.nav.foreldrepenger.historikk.util.annoteringer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;

import no.nav.foreldrepenger.historikk.util.validatorer.OrgnrValidator;

@Constraint(validatedBy = OrgnrValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Orgnr {

    Class<?>[] groups() default {};

    Class<?>[] payload() default {};

    String message() default "Nei gi deg, da";
}