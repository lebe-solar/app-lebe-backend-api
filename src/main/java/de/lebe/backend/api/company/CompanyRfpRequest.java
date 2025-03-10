package de.lebe.backend.api.company;

import jakarta.validation.constraints.NotNull;

/**
 * Represents the main request containing contact, building type, and system details.
 */
public record CompanyRfpRequest(
        @NotNull(message = "Contact details must be provided")
        CompanyContact contact,
        
        @NotNull(message = "Building type must be provided")
        BuildingType buildingType,
        
        @NotNull(message = "System details must be provided")
        CompanySystemDetails system
) {}

