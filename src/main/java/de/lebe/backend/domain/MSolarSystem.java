package de.lebe.backend.domain;

import lombok.Data;

@Data
public class MSolarSystem
{

	private String type;

	private String pvPower;
	private String energieStorageCapacity;
	
	private boolean wallBox;
	private boolean newMeterBox;
	private boolean energieStorage;
	
}
