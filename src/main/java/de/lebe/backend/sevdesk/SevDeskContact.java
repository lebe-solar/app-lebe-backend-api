package de.lebe.backend.sevdesk;

import lombok.Data;

@Data
public abstract class SevDeskContact {

	/**
	 * Technical identifier of the contact in the CRM.
	 */
	private Integer technicalId;
	/**
	 * Unique functional identifier.
	 */
	private String customerNumber;
	/**
	 * Mobile number.
	 */
	private String mobile;
	/**
	 * Email.
	 */
	private String email;
	/**
	 * Street with house number.
	 */
	private String streetWithHnr;
	/**
	 * Postal code.
	 */
	private String postalCode;
	/**
	 * City.
	 */
	private String city;
	
	public abstract String getQualifiedName();

}