package de.lebe.backend.api.company;

import java.time.LocalDateTime;

/**
 * Represents system-related details, including installation dates and requirements.
 */
public record CompanySystemDetails(
        LocalDateTime dateOfInstallation,
        
        LocalDateTime deadline,
        
        String requiredPV,
        
        String message
) {}