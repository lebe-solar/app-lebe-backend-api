package de.lebe.backend.domain;

import lombok.Data;

@Data
public class MPrivateCustomer  {
	
	private String name;
	
	private String lastname;
	
	private String gender;
	
	private String email;
	
	private String mobileNumber;
	
	private String streetWithHnr;
	
	private String postalCode; 
	
	private String city;
}
