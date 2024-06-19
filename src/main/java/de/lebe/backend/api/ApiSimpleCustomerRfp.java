package de.lebe.backend.api;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.lebe.backend.api.dto.CommonResponse;
import de.lebe.backend.api.dto.CustomerProposalRequest;
import de.lebe.backend.domain.MSubmissionResult;
import de.lebe.backend.process.ProcessSimpleCustomerRfp;
import de.lebe.backend.util.RequestCacheService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

@RestController
public class ApiSimpleCustomerRfp {

	@Autowired
	private ProcessSimpleCustomerRfp process;

	@Autowired
	private RequestCacheService requestCacheService;
	
	@Autowired
    private Validator validator;

	@PostMapping("/customer/rfp")
	public CommonResponse submitSimpleCustomerRpf(@RequestBody CustomerProposalRequest request,
			HttpServletRequest httpRequest) {
		
		Set<ConstraintViolation<CustomerProposalRequest>> violations = validator.validate(request);
        if (!violations.isEmpty()) {
            StringBuilder errors = new StringBuilder();
            for (ConstraintViolation<CustomerProposalRequest> violation : violations) {
                errors.append(violation.getMessage()).append("\n");
            }
            throw new IllegalArgumentException(errors.toString());
        }

		String clientIp = httpRequest.getRemoteAddr();

		if (requestCacheService.isDuplicateRequest(request.email(), clientIp)) {
			throw new IllegalArgumentException(
					"Es scheint, als ob wir bereits eine Anfrage von dieser E-Mail-Adresse erhalten haben. Bitte überprüfen Sie Ihre Angaben und versuchen Sie es erneut.");
		}
		

		try {
			process.processRfp(request);
		} catch (Exception e) {
			requestCacheService.remove(request.email(), clientIp);
			throw e;
		}

		return new CommonResponse("200",
				"Wir haben Ihre Anfrage erfolgreich erhalten und werden uns so schnell wie möglich bei Ihnen melden.");
	}

	@GetMapping("/advisor/rfp/approve")
	public MSubmissionResult approveCustomerRfp(@RequestParam String approvalToken) {

		var result = process.approveCustomerRfp(approvalToken);

		return result;

	}

	@GetMapping("/advisor/rfp/reject")
	public void rejectCustomerRfp(@RequestParam String approvalToken) {

		process.rejectCustomerRfp(approvalToken);

		return;
	}
}
