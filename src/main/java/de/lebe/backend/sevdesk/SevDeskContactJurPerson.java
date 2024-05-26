/**
 * 
 */
package de.lebe.backend.sevdesk;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author LaurenzTontsch
 *	Juristical person.
 */
@Data
@EqualsAndHashCode
public class SevDeskContactJurPerson extends SevDeskContact {

	private String organistationName;

	@Override
	public String getQualifiedName() {
		return organistationName;
	}
}
