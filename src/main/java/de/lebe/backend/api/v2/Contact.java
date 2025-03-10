package de.lebe.backend.api.v2;

import de.lebe.backend.api.ValidEmail;
import de.lebe.backend.api.ValidMobile;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record Contact(
		String gender, 
		@NotBlank String firstname, 
		@NotBlank String lastname, 
		@NotBlank @Email @ValidEmail(message = "Ungültige E-Mail-Adresse") String email,
		@NotBlank @ValidMobile String mobile, 
		String address) {
}
