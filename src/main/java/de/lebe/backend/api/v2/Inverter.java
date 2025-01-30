package de.lebe.backend.api.v2;

public record Inverter(
	    int id,
	    String name,
	    String model,
	    String power,
	    String unit
	) {}