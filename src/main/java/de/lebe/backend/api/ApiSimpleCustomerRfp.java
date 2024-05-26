package de.lebe.backend.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import de.lebe.backend.api.dto.CustomerProposalRequest;
import de.lebe.backend.process.ProcessSimpleCustomerRfp;
import de.lebe.backend.util.RequestCacheService;
import jakarta.servlet.http.HttpServletRequest;

@RestController
public class ApiSimpleCustomerRfp {

	@Autowired
	private ProcessSimpleCustomerRfp process;

	@Autowired
	private RequestCacheService requestCacheService;

	@PostMapping("/customer/rfp")
	public void submitSimpleCustomerRpf(@RequestBody CustomerProposalRequest request, HttpServletRequest httpRequest) {
		
		String clientIp = httpRequest.getRemoteAddr();
		
		if (requestCacheService.isDuplicateRequest(request.email(), clientIp)) {
			throw new IllegalArgumentException("Duplicate request from the same email and IP address.");
		}
		
		process.processRfp(request);
	}
}
