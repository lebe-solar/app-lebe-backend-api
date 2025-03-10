package de.lebe.backend.api.v2;

import java.util.List;

public record Appointment(
	    List<String> selectedProducts,
	    String message
	) {}
