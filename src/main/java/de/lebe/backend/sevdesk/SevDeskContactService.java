package de.lebe.backend.sevdesk;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.lebe.backend.sevdesk.api.SevDeskBaseDataClient;
import de.lebe.backend.sevdesk.api.SevDeskContactClient;
import de.lebe.backend.sevdesk.api.SevDeskFactoryClient;
import de.lebe.backend.sevdesk.model.CommunicationWay;
import de.lebe.backend.sevdesk.model.CommunicationWay.CommunicationWayContact;
import de.lebe.backend.sevdesk.model.CommunicationWay.CommunicationWayKey;
import de.lebe.backend.sevdesk.model.CommunicationWayKeyModel;
import de.lebe.backend.sevdesk.model.Contact;
import de.lebe.backend.sevdesk.model.ContactAddress;
import de.lebe.backend.sevdesk.model.ContactAddress.Category;
import de.lebe.backend.sevdesk.model.ContactAddress.ContactAddressContact;
import de.lebe.backend.sevdesk.model.ContactAddress.StaticCountry;
import de.lebe.backend.sevdesk.model.ContactAddressCategoryResponse;
import de.lebe.backend.sevdesk.model.FactoryNextNumberResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SevDeskContactService {

	@Autowired
	private SevDeskContactClient contactClient;
	
	@Autowired
	private SevDeskFactoryClient factoryClient;
	
	@Autowired
	private SevDeskBaseDataClient baseDataClient;

	/**
	 * Create a new  contact of either natural or juristical person in the CRM system of SevDesk.
	 * @param contact to create
	 * @return contact including technical id and customer number.
	 */
	public Integer createContact(SevDeskContact contact) {
		FactoryNextNumberResponse nextCustomerNumber = factoryClient.getNextCustomerNumber();
		
		var keyList = baseDataClient.getCommunicationWayKeys().objects()
				.stream()
				.collect(Collectors.toMap(CommunicationWayKeyModel::translationCode, CommunicationWayKeyModel::id));
		
		
		Contact mContact = new Contact();
		if(contact instanceof SevDeskContactNatPerson natPerson) {
			// Customer (ID: 3)
			mContact.getCategory().setId(3);
			mContact.getCategory().setObjectName("Category");

			mContact.setSurename(natPerson.getFirstname());
			mContact.setFamilyname(natPerson.getLastname());
			mContact.setGender(natPerson.getGender());
			
		} else if(contact instanceof SevDeskContactJurPerson jurPerson) {
			// Customer (ID: 2)
			mContact.getCategory().setId(2);
			mContact.getCategory().setObjectName("Category");
			mContact.setName(jurPerson.getOrganistationName());
		} 
		
		mContact.setCustomerNumber(nextCustomerNumber.objects());
		log.info("Create a customer number {} for nat={}, jur={} in SevDesk",mContact.getCustomerNumber(),  mContact.getFamilyname(), mContact.getName());

		var contactResponse = contactClient.createContact(mContact);
		var contactId = contactResponse.objects().getId();
		contact.setCustomerNumber(mContact.getCustomerNumber());
		contact.setTechnicalId(contactId);
		log.info("Create a contact with id {} for nat={}, jur={} in SevDesk", contactId,  mContact.getFamilyname(), mContact.getName());

		var phone = new CommunicationWay();
		phone.setContact(new CommunicationWayContact(contactId));
		phone.setType("MOBILE");
		// GET to /CommunicationWayKey.
		phone.setKey(new CommunicationWayKey(keyList.get("COMM_WAY_KEY_MOBILE")));
		phone.setValue(contact.getMobile());
		
		var resultphone = contactClient.createCommunicationWay(phone);
		log.info("Create a phone entry with id {} for nat={}, jur={} in SevDesk", 
				resultphone.objects().getId(),  
				mContact.getFamilyname(), mContact.getName());
		
		var email = new CommunicationWay();
		email.setContact(new CommunicationWayContact(contactId));
		email.setType("EMAIL");
		email.setKey(new CommunicationWayKey(keyList.get("COMM_WAY_KEY_PRIVAT")));
		email.setValue(contact.getEmail());
		
		var resultemail= contactClient.createCommunicationWay(email);
		log.info("Create a email entry with id {} for nat={}, jur={} in SevDesk", 
				resultemail.objects().getId(),  
				mContact.getFamilyname(), mContact.getName());
		

		// create address
		//TODO + Caching
		ContactAddressCategoryResponse addressCategories = contactClient.getAddressCategories();
		int privateAddressId = addressCategories.objects().stream()
				.filter(c -> c.translationCode().equalsIgnoreCase("CATEGORY_PRIVAT")).findFirst().orElseThrow().id();

		ContactAddress mAddress = new ContactAddress();
		mAddress.setCategory(new Category(privateAddressId));
		
		mAddress.setContact(new ContactAddressContact(contact.getTechnicalId()));
		mAddress.setStreet(contact.getStreetWithHnr());
		mAddress.setZip(contact.getPostalCode());
		mAddress.setCity(contact.getCity());
		// Germany
		mAddress.setCountry(new StaticCountry(1));
		
		var resultaddress= contactClient.createContactAddress(mAddress);
		log.info("Create a address entry with id {} for nat={}, jur={} in SevDesk", 
				resultaddress.objects().getId(),  
				mContact.getFamilyname(), mContact.getName());
		
		return contactId;
	}

}
