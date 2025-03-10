package de.lebe.backend.api.company;

import de.lebe.backend.api.ValidEmail;
import de.lebe.backend.api.ValidMobile;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * Represents the contact details of a person or company.
 */
public record CompanyContact(
        @NotBlank(message = "Company name cannot be blank")
        String companyName,
        
        @NotBlank(message = "Street address cannot be blank")
        String street,
        
        @Pattern(regexp = "\\d{5}", message = "Invalid postal code format")
        String postalCode,
        
        @NotBlank(message = "Place cannot be blank")
        String place,
        
        @NotBlank(message = "Country cannot be blank")
        String country,
        
        @Pattern(regexp = "[01]", message = "Gender must be '0' or '1'")
        String gender,
        
        @NotBlank(message = "First name cannot be blank")
        String firstname,
        
        @NotBlank(message = "Last name cannot be blank")
        String lastname,
        
    	@NotBlank @Email @ValidEmail(message = "Ungültige E-Mail-Adresse") String email,
    	
		@NotBlank @ValidMobile String mobile
) {}