package de.lebe.backend.api.v2;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import de.lebe.backend.api.dto.CommonResponse;
import de.lebe.backend.process.PrivateCustomerPVRequestProcess;
import de.lebe.backend.util.RequestCacheService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

@RestController
public class ApiPrivateCustomerPV {

	@Autowired
	private PrivateCustomerPVRequestProcess process;
	
	@Autowired
	private RequestCacheService requestCacheService;
	
	@Autowired
    private Validator validator;
	
	
	@PostMapping("/customer/private/pvrequest")
	public CommonResponse submitSimpleCustomerRpf(@RequestBody PrivateCustomerPVRequest request,
			HttpServletRequest httpRequest) {
		
		Set<ConstraintViolation<PrivateCustomerPVRequest>> violations = validator.validate(request);
        if (!violations.isEmpty()) {
            StringBuilder errors = new StringBuilder();
            for (ConstraintViolation<PrivateCustomerPVRequest> violation : violations) {
                errors.append(violation.getMessage()).append("\n");
            }
            throw new IllegalArgumentException(errors.toString());
        }

		String clientIp = httpRequest.getRemoteAddr();

		if (requestCacheService.isDuplicateRequest(request.contact().email(), clientIp)) {
			throw new IllegalArgumentException(
					"Es scheint, als ob wir bereits eine Anfrage von dieser E-Mail-Adresse erhalten haben. Bitte überprüfen Sie Ihre Angaben und versuchen Sie es erneut.");
		}
		

		try {
			process.sendRequestToAdvisor(request);
		} catch (Exception e) {
			requestCacheService.remove(request.contact().email(), clientIp);
			throw e;
		}

		return new CommonResponse("200",
				"Wir haben Ihre Anfrage erfolgreich erhalten und werden uns so schnell wie möglich bei Ihnen melden.");
	}
}
