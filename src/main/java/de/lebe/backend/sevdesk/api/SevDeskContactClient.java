package de.lebe.backend.sevdesk.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import de.lebe.backend.sevdesk.model.CommunicationWay;
import de.lebe.backend.sevdesk.model.Contact;
import de.lebe.backend.sevdesk.model.ContactAddress;
import de.lebe.backend.sevdesk.model.ContactAddressCategoryResponse;
import de.lebe.backend.sevdesk.model.ObjectResponse;

@FeignClient(value = "sevDeskContactClient", url = "${solar.order.crm.client.sevDesk}")
public interface SevDeskContactClient {

	@PostMapping(value = "Contact")
	ObjectResponse<Contact> createContact(@RequestBody Contact request);
	
	@PostMapping(value = "CommunicationWay")
	ObjectResponse<CommunicationWay> createCommunicationWay(@RequestBody CommunicationWay request);
	
	@PostMapping(value = "ContactAddress")
	ObjectResponse<ContactAddress> createContactAddress(ContactAddress mAddress);
	
	@GetMapping(value = "Category?objectType=ContactAddress")
	ContactAddressCategoryResponse getAddressCategories();
}
