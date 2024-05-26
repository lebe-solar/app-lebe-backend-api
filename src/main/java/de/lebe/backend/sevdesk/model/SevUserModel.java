package de.lebe.backend.sevdesk.model;

import java.time.ZonedDateTime;

import lombok.Data;

@Data
public class SevUserModel {
    private int id;
    private String objectName;
    private String additionalInformation;
    private ZonedDateTime create;
    private ZonedDateTime update;
    private String fullname;
    private String firstName;
    private String lastName;
    private String username;
    private int status;
    private String email;
    private String gender;
    private String role;
    private String memberCode;
    private SevClient sevClient;
    private ZonedDateTime lastLogin;
    private String lastLoginIp;
    private int welcomeScreenSeen;
    private String smtpName;
    private String smtpMail;
    private String smtpUser;
    private int smtpPort;
    private String smtpSsl;
    private String smtpHost;
    private String languageCode;
    private int twoFactorAuth;
    private int forcePasswordChange;
    private int clientOwner;
    private int defaultReceiveMailCopy;
    private int hideMapsDirections;
    private ZonedDateTime startDate;
    private ZonedDateTime endDate;
    private ZonedDateTime lastPasswordChange;

    @Data
    public static class SevClient {
        private String id;
        private String objectName;
    }
}
