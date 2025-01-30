package de.lebe.backend.api.v2;

public record ProductDetails(
	    int solarModuleId,
	    int solarModuleCount,
	    int inverterId,
	    int interterCount,
	    int storageId,
	    int storageCount
	) {}
