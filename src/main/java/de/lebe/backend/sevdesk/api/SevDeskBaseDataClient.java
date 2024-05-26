package de.lebe.backend.sevdesk.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import de.lebe.backend.sevdesk.model.CommunicationWayKeyModel;
import de.lebe.backend.sevdesk.model.ObjectListResponse;
import de.lebe.backend.sevdesk.model.SevUserModel;

@FeignClient(value = "sevDeskBaseDataClient", url = "${solar.order.crm.client.sevDesk}")
public interface SevDeskBaseDataClient {

	@GetMapping(value = "SevUser")
	ObjectListResponse<SevUserModel> getUsers();
	
	
	@GetMapping(value = "CommunicationWayKey")
	ObjectListResponse<CommunicationWayKeyModel> getCommunicationWayKeys();
	
}
