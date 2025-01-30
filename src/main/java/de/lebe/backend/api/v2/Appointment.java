package de.lebe.backend.api.v2;

public record Appointment(
	    String phoneAppointment,
	    String inPersonAppointment,
	    String deliveryAppointment,
	    String message
	) {}
