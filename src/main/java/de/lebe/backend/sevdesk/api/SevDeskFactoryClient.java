package de.lebe.backend.sevdesk.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import de.lebe.backend.sevdesk.model.FactoryNextNumberResponse;

@FeignClient(value = "sevDeskFactoryClient", url = "${solar.order.crm.client.sevDesk}")
public interface SevDeskFactoryClient {

	@GetMapping(value = "Contact/Factory/getNextCustomerNumber")
	FactoryNextNumberResponse getNextCustomerNumber();
	
	@GetMapping(value = "Order/Factory/getNextOrderNumber")
	FactoryNextNumberResponse getNextOrderNumber();
}
