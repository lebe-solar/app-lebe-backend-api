package de.lebe.backend.api;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class AddressValidator implements ConstraintValidator<ValidAddress, String> {

    // Regular Expression to check if the address contains a number
    private static final String ADDRESS_REGEX = ".*\\d+.*";

    @Override
    public void initialize(ValidAddress constraintAnnotation) {
        // No initialization required
    }

    @Override
    public boolean isValid(String address, ConstraintValidatorContext context) {
        if (address == null || address.trim().isEmpty()) {
            return false;
        }
        return address.matches(ADDRESS_REGEX);
    }
}