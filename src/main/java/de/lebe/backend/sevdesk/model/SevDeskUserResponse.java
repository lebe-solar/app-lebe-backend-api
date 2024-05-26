package de.lebe.backend.sevdesk.model;

import java.time.ZonedDateTime;
import java.util.List;

public record SevDeskUserResponse(List<SevUser> objects) {
    public record SevUser(
        String id,
        String objectName,
        String additionalInformation,
        ZonedDateTime create,
        ZonedDateTime update,
        String fullname,
        String firstName,
        String lastName,
        String username,
        String status,
        String email,
        String gender,
        String role,
        String memberCode,
        SevClient sevClient,
        ZonedDateTime lastLogin,
        String lastLoginIp,
        String welcomeScreenSeen,
        String smtpName,
        String smtpMail,
        String smtpUser,
        String smtpPort,
        String smtpSsl,
        String smtpHost,
        String languageCode,
        String twoFactorAuth,
        String forcePasswordChange,
        String clientOwner,
        String defaultReceiveMailCopy,
        String hideMapsDirections,
        ZonedDateTime startDate,
        ZonedDateTime endDate,
        ZonedDateTime lastPasswordChange
    ) {
        public record SevClient(
            String id,
            String objectName
        ) {}
    }
}

