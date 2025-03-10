package de.lebe.backend.graphql;

import java.util.Base64;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.microsoft.graph.models.Attachment;
import com.microsoft.graph.models.BodyType;
import com.microsoft.graph.models.EmailAddress;
import com.microsoft.graph.models.FileAttachment;
import com.microsoft.graph.models.ItemBody;
import com.microsoft.graph.models.Message;
import com.microsoft.graph.models.Recipient;
import com.microsoft.graph.serviceclient.GraphServiceClient;
import com.microsoft.graph.users.item.sendmail.SendMailPostRequestBody;

import de.lebe.backend.api.company.CompanyRfpRequest;
import de.lebe.backend.api.v2.Appointment;
import de.lebe.backend.api.v2.Contact;
import de.lebe.backend.api.v2.ExistingCustomerMessage;
import de.lebe.backend.api.v2.PrivateCustomerPVRequest;
import de.lebe.backend.api.v2.PvSystem;
import de.lebe.backend.api.v2.SelectedOffer;
import de.lebe.backend.domain.LeBeFileAttachment;
import de.lebe.backend.process.AzureBlobStorageService;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MailServiceForPrivateCustomer {

	@Value("${spring.mail.username}")
	private String from;

	@Autowired
	private TemplateEngine templateEngine;

	@Autowired
	private GraphServiceClient client;

	@Value("${solar.baseurl}")
	private String baseUrl;

	@Autowired
	private AzureBlobStorageService attchmentService;
	
	
	public void sendMessageMailToBusiness(CompanyRfpRequest request) {
		log.info("Sending RFP mail to business");
		Context context = new Context();

		// Contact Information
		context.setVariable("contact", request.contact());
		context.setVariable("system", request.system());
		context.setVariable("buildingType", request.buildingType().name());

		Message message = new Message();
		message.setSubject("Nachricht von " + request.contact().companyName());

		ItemBody body = new ItemBody();
		body.setContentType(BodyType.Html);
		body.setContent(templateEngine.process("companyRfp", context));
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
	
	
	
	public void sendMessageMailToBusiness(ExistingCustomerMessage contact) {
		log.info("Sending RFP mail to business");
		Context context = new Context();

		// Contact Information
		context.setVariable("firstname", contact.firstname());
		context.setVariable("lastname", contact.lastname());
		context.setVariable("email", contact.email());
		context.setVariable("mobile", contact.mobile());
		context.setVariable("address", contact.streetWithHnr() + ", " + contact.postalCode() + " " + contact.city());
		context.setVariable("gender", "0".equalsIgnoreCase(contact.gender()) ? "Herr" : "Frau");

		context.setVariable("message", contact.customMessage());
		

		Message message = new Message();
		message.setSubject("Nachricht von " + contact.lastname());

		ItemBody body = new ItemBody();
		body.setContentType(BodyType.Html);
		body.setContent(templateEngine.process("existingCustomer", context));
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

	public void sendPVRequestMailToBusiness(PrivateCustomerPVRequest customerRfp, byte[] houseImage) {
		log.info("Sending RFP mail to business");
		Context context = new Context();

		// Contact Information
		Contact contact = customerRfp.contact();
		context.setVariable("contact", contact);
		context.setVariable("firstname", contact.firstname());
		context.setVariable("lastname", contact.lastname());
		context.setVariable("email", contact.email());
		context.setVariable("mobile", contact.mobile());
		context.setVariable("address", contact.address());
		context.setVariable("gender", "0".equalsIgnoreCase(contact.gender()) ? "Herr" : "Frau");

		// PV System Details
		PvSystem pvSystem = customerRfp.pvSystem();

		if (pvSystem != null) {

			// Module Details
			context.setVariable("moduleName", pvSystem.module().model() + " " + pvSystem.module().name());
			context.setVariable("moduleQuantity", pvSystem.module_quantity());

			// Inverter Details
			context.setVariable("inverterName", pvSystem.inverter().model() + " " + pvSystem.inverter().name());
			context.setVariable("inverterQuantity", pvSystem.inverter_quantity());

			// Battery Details
			context.setVariable("batteryName", pvSystem.battery().model() + " " + pvSystem.battery().name());
			context.setVariable("batteryQuantity", pvSystem.battery_quantity());

			// Selected Offer
			SelectedOffer selectedOffer = pvSystem.selectedOffer();
			context.setVariable("selectedOffer", selectedOffer);
			context.setVariable("offerTitle", selectedOffer.title());
			context.setVariable("offerSubtitle", selectedOffer.subtitle());
			context.setVariable("offerDescription", selectedOffer.description());
			context.setVariable("offerConditions", selectedOffer.conditions());
			context.setVariable("offerValidUntil", selectedOffer.validUntil());
			context.setVariable("offerDesignedFor", selectedOffer.designedFor());
			context.setVariable("offerPrice", selectedOffer.price());
			context.setVariable("offerLink", selectedOffer.link());
			context.setVariable("offerPreviewImage", selectedOffer.previewImage());

			// Included Items in the Offer
			context.setVariable("inclusiveItems", selectedOffer.inclusive());
		}

		// Appointment Details
		Appointment appointment = customerRfp.appointment();
		context.setVariable("message", appointment.message());
		context.setVariable("furtherProducts", appointment.selectedProducts());

		if (houseImage != null) {
			String base64EncodedImage = Base64.getEncoder().encodeToString(houseImage);
			context.setVariable("base64EncodedImage", base64EncodedImage);
		}

		Message message = new Message();
		if(customerRfp.pvSystem() != null) {
			message.setSubject("NEU ANFRAGE von " + customerRfp.contact().lastname() + " für ein: "
					+ customerRfp.pvSystem().selectedOffer().title());
		} else {
			message.setSubject("Nachricht von " + customerRfp.contact().lastname());
		}

		ItemBody body = new ItemBody();
		body.setContentType(BodyType.Html);
		body.setContent(templateEngine.process("privateCustomerPVRequest", context));
		message.setBody(body);

		// 🔹 Lade Dateien aus Azure Blob Storage für die E-Mail-Adresse
		List<LeBeFileAttachment> fileAttachments = attchmentService.getFilesForEmail(customerRfp.contact().email());
		// 🔹 Konvertiere Dateien in Microsoft Graph API Anhänge
		LinkedList<Attachment> attachments = new LinkedList<>();
		for (var file : fileAttachments) {
			FileAttachment attachment = new FileAttachment();
			attachment.setName(file.name());
			attachment.setContentType("application/octet-stream"); // Falls bekannt, spezifizieren
			attachment.setContentBytes(file.content());
			attachments.add(attachment);
		}
		message.setAttachments(attachments);

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