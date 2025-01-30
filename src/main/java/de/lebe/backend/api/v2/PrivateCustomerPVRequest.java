package de.lebe.backend.api.v2;

public record PrivateCustomerPVRequest(Contact contact, PvSystem pvSystem, Appointment appointment) {
}
