package de.lebe.backend.graphql;

import java.util.Base64;
import java.util.LinkedList;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.microsoft.graph.models.BodyType;
import com.microsoft.graph.models.EmailAddress;
import com.microsoft.graph.models.ItemBody;
import com.microsoft.graph.models.Message;
import com.microsoft.graph.models.Recipient;
import com.microsoft.graph.serviceclient.GraphServiceClient;
import com.microsoft.graph.users.item.sendmail.SendMailPostRequestBody;

import de.lebe.backend.domain.MCustomerRfp;
import de.lebe.backend.mapper.MCustomerRfpMapper;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MailService {

	@Value("${spring.mail.username}")
	private String from;

	@Autowired
	private TemplateEngine templateEngine;

	@Autowired
	private GraphServiceClient client;
	
	@Value("${solar.baseurl}")
	private String baseUrl;

	/**
	 * { "gender": 1,
	 * 
	 * "mobile": "+491234567890", "email": "lthntsch@hotmail.com", "streetWithHnr":
	 * "Musterstraße 1", "postalCode": "12345", "city": "Musterstadt",
	 * 
	 * "pvPower": "5 kW", "energieStorageCapacity": "10 kWh",
	 * 
	 * "roofTopType": "Flachdach", "houseType": "Einfamilienhaus",
	 * 
	 * "wallBox": true, "newMeterBox": false, "energieStorage": true,
	 * 
	 * "energieUsage": "5000 kWh/year",
	 * 
	 * "pvPackageType": 2,
	 * 
	 * "customMessage": "Bitte kontaktieren Sie mich für weitere Details." }
	 * 
	 * @param customerRfp
	 * @param houseImage 
	 */
	public void sendRfpMailToBusiness(MCustomerRfp customerRfp, byte[] houseImage) {
		log.info("Sending RFP mail to business");
		String id = MCustomerRfpMapper.getPartionKey(customerRfp);

		Context context = new Context();
		context.setVariable("acceptUrl",
				baseUrl + "/advisor/rfp/approve?approvalToken=" + id);
		context.setVariable("rejectUrl",
				baseUrl + "/advisor/rfp/reject?approvalToken=" + id);

		context.setVariable("header",
				customerRfp.getCustomer().getName() + " " + customerRfp.getCustomer().getLastname());

		context.setVariable("message", customerRfp.getCustomerInput().getMessage());

		context.setVariable("house",
				customerRfp.getHouseDetails().getHouseType() + " " + customerRfp.getHouseDetails().getRoofTopType());
		context.setVariable("pv", customerRfp.getSolarSystem().getPvPower());
		context.setVariable("energieStorage", customerRfp.getSolarSystem().getEnergieStorageCapacity());
		context.setVariable("wallBox", customerRfp.getSolarSystem().isWallBox() ? "Ja" : "Nein");
		context.setVariable("newMeterBox", customerRfp.getSolarSystem().isNewMeterBox() ? "Ja" : "Nein");

		context.setVariable("city",
				customerRfp.getCustomer().getPostalCode() + " " + customerRfp.getCustomer().getCity());
		context.setVariable("street", customerRfp.getCustomer().getStreetWithHnr());
		context.setVariable("mail", customerRfp.getCustomer().getEmail());
		context.setVariable("mobile", customerRfp.getCustomer().getMobileNumber());

		String base64EncodedImage = Base64.getEncoder().encodeToString(houseImage);
        context.setVariable("base64EncodedImage", base64EncodedImage);

		Message message = new Message();
		message.setSubject("NEU ANFRAGE - " + customerRfp.getSolarSystem().getPvPower() + "kWp - "
				+ customerRfp.getCustomer().getCity());

		ItemBody body = new ItemBody();
		body.setContentType(BodyType.Html);
		body.setContent(templateEngine.process("message_to_business", context));
		message.setBody(body);

		var email = new SendMailPostRequestBody();

		// TO
		LinkedList<Recipient> toRecipients = new LinkedList<Recipient>();
		Recipient recipient = new Recipient();
		EmailAddress emailAddress = new EmailAddress();
		emailAddress.setAddress("kontakt@lebe-solarenergie.de");
		recipient.setEmailAddress(emailAddress);
		toRecipients.add(recipient);
		message.setToRecipients(toRecipients);

		// CC
		LinkedList<Recipient> ccRecipients = new LinkedList<Recipient>();
		Recipient recipient1 = new Recipient();
		EmailAddress emailAddress1 = new EmailAddress();
		emailAddress1.setAddress("s.leu@lebe-solarenergie.de");
		recipient1.setEmailAddress(emailAddress1);
		ccRecipients.add(recipient1);
		message.setCcRecipients(ccRecipients);

		email.setSaveToSentItems(false);
		email.setMessage(message);

		var users = client.users().get().getValue();

		var user = users.stream().filter(u -> {
			String mail = u.getUserPrincipalName();

			return StringUtils.containsIgnoreCase(mail, "kontakt");
		}).findFirst().orElseGet(() -> users.get(0));

		client.users().byUserId(user.getId()).sendMail().post(email);

	}

}