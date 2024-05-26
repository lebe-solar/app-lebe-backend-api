package de.lebe.backend.sevdesk;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Domain object of an CRM contact
 * @author LaurenzTontsch
 *
 */
@Data
@EqualsAndHashCode
public class SevDeskContactNatPerson extends SevDeskContact {
	
	/**
	 * Gender in format of Herr / Frau. (Ger)
	 */
	private String gender;
	
	/**
	 * Firstname.
	 */
	private String firstname;
	
	/**
	 * Lastname.
	 */
	private String lastname;

	@Override
	public String getQualifiedName() {
		return new StringBuilder()
				.append(firstname)
				.append(" ")
				.append(lastname)
				.toString();
	}

}
