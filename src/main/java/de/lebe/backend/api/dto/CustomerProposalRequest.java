package de.lebe.backend.api.dto;

import de.lebe.backend.api.ValidAddress;
import de.lebe.backend.api.ValidEmail;
import de.lebe.backend.api.ValidMobile;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CustomerProposalRequest(
        /** Herr | Frau */
        int gender,
        @NotBlank String firstname,
        @NotBlank String lastname,
        @NotBlank @ValidMobile String mobile,
        @NotBlank @Email @ValidEmail(message = "Ungültige E-Mail-Adresse") String email,
        @NotBlank  @ValidAddress String streetWithHnr,
        @NotBlank(message = "Bitte geben Sie eine PLZ an") String postalCode,
        @NotBlank(message = "Bitte geben Sie eine Stadt an") String city,

        String pvPower,
        String energieStorageCapacity,

        String roofTopType,
        String houseType,

        boolean wallBox,
        boolean newMeterBox,
        boolean energieStorage,

        String energieUsage,

        int pvPackageType,

        String customMessage
) {
}
