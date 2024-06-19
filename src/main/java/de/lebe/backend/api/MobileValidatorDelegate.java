package de.lebe.backend.api;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class MobileValidatorDelegate implements ConstraintValidator<ValidMobile, String> {

	private static final PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {

		try {
            PhoneNumber number = phoneNumberUtil.parse(value, "DE");
            
            return phoneNumberUtil.isValidNumber(number);
        } catch (NumberParseException e) {
           
            return false;
        }
	}

}
