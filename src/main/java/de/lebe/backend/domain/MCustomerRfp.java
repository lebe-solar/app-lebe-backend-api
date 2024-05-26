package de.lebe.backend.domain;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;

import com.azure.spring.data.cosmos.core.mapping.Container;
import com.azure.spring.data.cosmos.core.mapping.PartitionKey;

import lombok.Data;

@Container(containerName = "customerRfp")
@Data
public class MCustomerRfp {

	@Id
	private String technicalId;
	
	@PartitionKey
	private String customerEmail;
	
	private LocalDateTime submittionDate;
	
	private MPrivateCustomer customer;
	
	private MSolarSystem solarSystem;
	
	private MCustomerConsumption customerConsumption;
	
	private MHouseDetails houseDetails;
	
	private MCustomerInput customerInput;
}
