package de.lebe.backend.api.v2;

import de.lebe.backend.api.ValidAddress;
import de.lebe.backend.api.ValidEmail;
import de.lebe.backend.api.ValidMobile;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ExistingCustomerMessage(String gender, 
		
		@NotBlank String firstname, 
		@NotBlank String lastname, 
		@NotBlank @Email @ValidEmail(message = "Ungültige E-Mail-Adresse") String email, 
		@NotBlank @ValidMobile String mobile, 
		@NotBlank  @ValidAddress String streetWithHnr,
		@NotBlank(message = "Bitte geben Sie eine PLZ an") String postalCode, 
		@NotBlank(message = "Bitte geben Sie eine Stadt an") String city, 
		String customMessage) {

}
