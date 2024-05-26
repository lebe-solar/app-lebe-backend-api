package de.lebe.backend.sevdesk.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import de.lebe.backend.sevdesk.model.OrderRequest;
import de.lebe.backend.sevdesk.model.OrderResponse;

@FeignClient(value = "sevDeskOrderClient", url = "${solar.order.crm.client.sevDesk}")
public interface SevDeskOrderClient {

	@PostMapping(value = "Order/Factory/saveOrder")
	OrderResponse createOrder(@RequestBody OrderRequest request);
}
