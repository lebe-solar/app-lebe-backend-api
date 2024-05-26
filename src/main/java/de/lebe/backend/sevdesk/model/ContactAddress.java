package de.lebe.backend.sevdesk.model;

import java.time.ZonedDateTime;

import lombok.Data;

@Data
public class ContactAddress {

	/**
	 * Default C'tor.
	 */
	public ContactAddress() {
	}

	private Integer id;
	private ZonedDateTime created;
	private ZonedDateTime update;

	/**
	 * The contact to which this contact address belongs.
	 */
	private ContactAddressContact contact;

	/**
	 * Street name.
	 */
	private String street;

	/**
	 * Zip code.
	 */
	private String zip;

	/**
	 * City name.
	 */
	private String city;

	/**
	 * Country of the contact address. For all countries, send a GET to
	 * /StaticCountry.
	 */
	private StaticCountry country;

	/**
	 * Category of the contact address. For all categories, send a GET to
	 * /Category?objectType=ContactAddress.
	 */
	private Category category;

	/**
	 * Name in address.
	 */
	private String name;

	/**
	 * Second name in address.
	 */
	private String name2;

	/**
	 * Third name in address.
	 */
	private String name3;

	/**
	 * Fourth name in address.
	 */
	private String name4;

	/**
	 * Represents the contact entity.
	 */
	@Data
	public static class ContactAddressContact {
		
		public ContactAddressContact() {
		}
		public ContactAddressContact(int id) {
			this.id = id;
		}
		
		private int id;
		private String objectName = "Contact";
	}

	/**
	 * Represents the static country entity.
	 */
	@Data
	public static class StaticCountry {
		public StaticCountry() {
		}
		public StaticCountry(int id) {
			this.id = id;
		}
		private int id;
		private String objectName = "StaticCountry";
	}

	/**
	 * Represents the category entity.
	 */
	@Data
	public static class Category {
		
		public Category() {
		}
		public Category(int id) {
			this.id = id;
		}
		
		private int id;
		private String objectName = "Category";
	}
}
