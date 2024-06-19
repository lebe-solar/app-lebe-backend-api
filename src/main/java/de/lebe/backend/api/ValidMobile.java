package de.lebe.backend.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Constraint(validatedBy = MobileValidatorDelegate.class)
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidMobile {
    String message() default "Bitte geben Sie eine korrekte inländische Mobilenummer an";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}