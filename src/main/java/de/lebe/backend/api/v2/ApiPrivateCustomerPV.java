package de.lebe.backend.api.v2;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import de.lebe.backend.api.dto.CommonResponse;
import de.lebe.backend.process.PrivateCustomerPVRequestProcess;
import de.lebe.backend.util.RequestCacheService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

@RestController
public class ApiPrivateCustomerPV {
	
	private static final Logger logger = LoggerFactory.getLogger(ApiPrivateCustomerPV.class);

	@Autowired
	private PrivateCustomerPVRequestProcess process;
	
	@Autowired
	private RequestCacheService requestCacheService;
	
	@Autowired
    private Validator validator;
	
	@Value("${solar.apikey}")
	String apiKey;
	
	@PostMapping("/customer/existing/message")
	public CommonResponse submitMessageFromExisting(@RequestHeader("x-api-key") String code,@RequestBody ExistingCustomerMessage request,
			HttpServletRequest httpRequest) {
		
		if(!apiKey.equals(code)) {
    		throw new AccessDeniedException("Invalid apikey");
    	}
		
		Set<ConstraintViolation<ExistingCustomerMessage>> violations = validator.validate(request);
        if (!violations.isEmpty()) {
            StringBuilder errors = new StringBuilder();
            for (ConstraintViolation<ExistingCustomerMessage> violation : violations) {
                errors.append(violation.getMessage()).append("\n");
            }
            logger.error("Invalid request {} due to {}", request, errors);
            
            throw new IllegalArgumentException(errors.toString());
        }

		String clientIp = httpRequest.getRemoteAddr();

		if (requestCacheService.isDuplicateRequest(request.email(), clientIp)) {
			throw new IllegalArgumentException(
					"Es scheint, als ob wir bereits eine Anfrage von dieser E-Mail-Adresse erhalten haben. Bitte überprüfen Sie Ihre Angaben und versuchen Sie es erneut.");
		}
		

		try {
			process.sendMessageToAdvisor(request);
		} catch (Exception e) {
			requestCacheService.remove(request.email(), clientIp);
			throw e;
		}

		return new CommonResponse("200",
				"Wir haben Ihre Anfrage erfolgreich erhalten und werden uns so schnell wie möglich bei Ihnen melden.");
	}
	
	
	@PostMapping("/customer/private/pvrequest")
	public CommonResponse submitSimpleCustomerRpf(@RequestHeader("x-api-key") String code,@RequestBody PrivateCustomerPVRequest request,
			HttpServletRequest httpRequest) {
		
		if(!apiKey.equals(code)) {
    		throw new AccessDeniedException("Invalid apikey");
    	}
		
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
