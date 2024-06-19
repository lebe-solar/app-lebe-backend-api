package de.lebe.backend.api;

import org.apache.commons.validator.routines.EmailValidator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EmailValidatorDelegete implements ConstraintValidator<ValidEmail, String> {

	private static EmailValidator validator = EmailValidator.getInstance(); 


    @Override
    public void initialize(ValidEmail constraintAnnotation) {
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        return validator.isValid(email);
    }
}