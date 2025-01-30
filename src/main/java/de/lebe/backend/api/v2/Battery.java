package de.lebe.backend.api.v2;

public record Battery(
	    int id,
	    String name,
	    String model,
	    String power,
	    String unit
	) {}