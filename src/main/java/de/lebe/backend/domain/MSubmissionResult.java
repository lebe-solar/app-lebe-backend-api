package de.lebe.backend.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MSubmissionResult {

	private MCustomerRfp customerRfp;
	
	private String cutomerNumber;
	
	private String orderNumber;
}
