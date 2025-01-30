package de.lebe.backend.process;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.lebe.backend.api.v2.PrivateCustomerPVRequest;
import de.lebe.backend.bing.AddressBingService;
import de.lebe.backend.bing.MAddressFormatted;
import de.lebe.backend.graphql.MailServiceForPrivateCustomer;

@Service
public class PrivateCustomerPVRequestProcess {

	@Autowired
	private MailServiceForPrivateCustomer mail;

	@Autowired
	private AddressBingService bingService;

	public void sendRequestToAdvisor(PrivateCustomerPVRequest request) {

		MAddressFormatted address = null;
		
		try {
			// "Allensteiner Str. 19, 63110 Rodgau, Deutschland"
			var addressParts = request.contact().address().split(", ");

			// String streetWithHnr, String city, String postalCode
			address = bingService.formatAddress(addressParts[0], addressParts[1].split(" ")[1],
					addressParts[1].split(" ")[0]);

			if (!address.isQualified() || !address.getCountryRegion().equals("Germany")) {
				throw new IllegalArgumentException("Die Addresse konnte nicht gefunden werden.");
			}
		} catch (Exception e) {

		}

		mail.sendPVRequestMailToBusiness(request, address.getImage());

	}
}
