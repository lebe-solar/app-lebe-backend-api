package de.lebe.backend.sevdesk;

import java.time.LocalDate;

import lombok.Data;

@Data
public class SevDeskOrderRequestForProposal {
	
	private LocalDate date;

	private String pvPower;
	private String energieStorageCapacity;
	
	private String roofTopType;
	private String houseType;
	
	private boolean wallBox;
	private boolean newMeterBox;
	private boolean energieStorage;
	
	private String customMessage;
}
