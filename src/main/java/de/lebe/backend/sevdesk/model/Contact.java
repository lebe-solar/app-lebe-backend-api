package de.lebe.backend.sevdesk.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

@Data
@EqualsAndHashCode
public class Contact extends BaseModel {
	
	/**
	 * The parent contact to which this contact belongs. Must be an organization.
	 */
	private ParentContact parent;
	
	
	
	
	/**
	 * The organization name. Be aware that the type of contact will depend on this
	 * attribute. If it holds a value, the contact will be regarded as an
	 * organization.
	 */
	private String name;

	/**
	 * Defines the status of the contact. 100 <-> Lead - 500 <-> Pending - 1000 <->
	 * Active.
	 */
	private int status = 100;

	/**
	 * The customer number.
	 */
	private String customerNumber;

	/**
	 * The first name of the contact. Not to be used for organizations.
	 */
	private String surename;

	/**
	 * The last name of the contact. Not to be used for organizations.
	 */
	private String familyname;

	/**
	 * A non-academic title for the contact. Not to be used for organizations.
	 */
	private String titel;

	/**
	 * Category of the contact. For more information, see here.
	 */
	private ContactCategory category = new ContactCategory();

	/**
	 * A description for the contact.
	 */
	private String description;

	/**
	 * A academic title for the contact. Not to be used for organizations.
	 */
	private String academicTitle;

	/**
	 * Gender of the contact. Not to be used for organizations.
	 */
	private String gender;

	/**
	 * Second name of the contact. Not to be used for organizations.
	 */
	private String name2;

	/**
	 * Birthday of the contact. Not to be used for organizations.
	 */
	private String birthday;

	/**
	 * Vat number of the contact.
	 */
	private String vatNumber;

	/**
	 * Bank account number (IBAN) of the contact.
	 */
	private String bankAccount;

	/**
	 * Bank number of the bank used by the contact.
	 */
	private String bankNumber;

	/**
	 * Absolute time in days which the contact has to pay his invoices and
	 * subsequently get a cashback.
	 */
	private Integer defaultCashbackTime;

	/**
	 * Percentage of the invoice sum the contact gets back if he payed invoices in
	 * time.
	 */
	private Float defaultCashbackPercent;

	/**
	 * The payment goal in days which is set for every invoice of the contact.
	 */
	private Integer defaultTimeToPay;

	/**
	 * The tax number of the contact.
	 */
	private String taxNumber;

	/**
	 * The tax office of the contact (only for Greek customers).
	 */
	private String taxOffice;

	/**
	 * Defines if the contact is freed from paying VAT.
	 */
	private short exemptVat;

	/**
	 * Defines which tax regulation the contact is using.
	 */
	private String taxType;

	/**
	 * Tax set which is used in every invoice of the contact.
	 */
	private ContactTaxSet taxSet;

	/**
	 * The default discount the contact gets for every invoice. Depending on
	 * defaultDiscountPercentage attribute, in percent or absolute value.
	 */
	private Float defaultDiscountAmount;

	/**
	 * Defines if the discount is a percentage (true) or an absolute value (false).
	 */
	private short defaultDiscountPercentage;

	/**
	 * Buyer reference of the contact.
	 */
	private String buyerReference;

	/**
	 * Defines whether the contact is a government agency (true) or not (false).
	 */
	private short governmentAgency;
	
	/**
	 * Represents the category of the contact.
	 */
	@RequiredArgsConstructor
	@Data
	public static class ParentContact {
		/**
		 * 
		 * Id of the parent contact.
		 * 
		 */
		private final Integer id;
		private String objectName = "Contact";
	}

	/**
	 * Represents the category of the contact.
	 */
	@Data
	public static class ContactCategory {
		/**
		 * 
		 * Supplier (ID: 2) Customer (ID: 3) Partner (ID: 4) Prospect Customer (ID: 28)
		 * 
		 */
		private int id = 3;
		private String objectName = "Category";
	}

	/**
	 * Represents the tax set used in every invoice of the contact.
	 */
	@Data
	public static class ContactTaxSet {
		private int id;
		private String objectName = "TaxSet";
	}
}
