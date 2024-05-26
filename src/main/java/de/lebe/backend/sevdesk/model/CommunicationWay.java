package de.lebe.backend.sevdesk.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class CommunicationWay  extends BaseModel {

	
	/**
	 * The contact to which this communication way belongs.
	 */
	private CommunicationWayContact contact;
	
	/**
	 * Type of the communication way. Enum: "EMAIL" "PHONE" "WEB" "MOBILE"
	 */
	private String type;

	/**
	 * The value of the communication way. For example, the phone number, email
	 * address, or website.
	 */
	private String value;

	/**
	 * The key of the communication way. Similar to the category of addresses. For
	 * all communication way keys, please send a GET to /CommunicationWayKey.
	 */
	private CommunicationWayKey key;

	/**
	 * Defines whether the communication way is the main communication way for the
	 * contact.
	 * Boolean as 0 | 1
	 */
	private Short main;
	
	/**
	 * Represents the communication way key entity.
	 */
	@Data
	public static class CommunicationWayContact {
	
		public CommunicationWayContact() {
		}
		
		public CommunicationWayContact(int id) {
			super();
			this.id = id;
		}
		/**
		 * Unique identifier of the key.
		 */
		private int id;
		private String objectName = "Contact";
	}

	/**
	 * Represents the communication way key entity.
	 */
	@Data
	public static class CommunicationWayKey {
		
		public CommunicationWayKey() {
		}
		
		public CommunicationWayKey(int id) {
			super();
			this.id = id;
		}
		
		/**
		 * Unique identifier of the key.
		 */
		private int id;
		private String objectName = "CommunicationWayKey";
	}
}
