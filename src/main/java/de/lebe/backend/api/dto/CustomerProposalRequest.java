package de.lebe.backend.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CustomerProposalRequest(
        /** Herr | Frau */
        int gender,
        @NotBlank String firstname,
        @NotBlank String lastname,
        @NotBlank String mobile,
        @NotBlank @Email String email,
        @NotBlank String streetWithHnr,
        @NotBlank String postalCode,
        @NotBlank String city,

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
