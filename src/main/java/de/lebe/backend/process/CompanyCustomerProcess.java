package de.lebe.backend.process;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.lebe.backend.api.company.CompanyRfpRequest;
import de.lebe.backend.api.v2.ExistingCustomerMessage;
import de.lebe.backend.graphql.MailServiceForPrivateCustomer;

@Service
public class CompanyCustomerProcess {

	@Autowired
	private MailServiceForPrivateCustomer mail;

	
	public void sendMessageToAdvisor(CompanyRfpRequest message) {

		mail.sendMessageMailToBusiness(message);

	}

}
