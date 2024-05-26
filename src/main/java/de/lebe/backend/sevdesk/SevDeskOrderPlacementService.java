package de.lebe.backend.sevdesk;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import de.lebe.backend.sevdesk.api.SevDeskBaseDataClient;
import de.lebe.backend.sevdesk.api.SevDeskFactoryClient;
import de.lebe.backend.sevdesk.api.SevDeskOrderClient;
import de.lebe.backend.sevdesk.model.FactoryNextNumberResponse;
import de.lebe.backend.sevdesk.model.Order;
import de.lebe.backend.sevdesk.model.Order.AddressCountry;
import de.lebe.backend.sevdesk.model.Order.Contact;
import de.lebe.backend.sevdesk.model.Order.SevUser;
import de.lebe.backend.sevdesk.model.OrderPosition;
import de.lebe.backend.sevdesk.model.OrderPosition.MeasurementUnit;
import de.lebe.backend.sevdesk.model.OrderRequest;

@Service
public class SevDeskOrderPlacementService {
	
	@Autowired
	private SevDeskOrderClient orderClient;
	
	@Autowired
	private SevDeskFactoryClient factoryClient;
	
	@Autowired
	private SevDeskBaseDataClient baseDataClient;
	
	public String getOrderNumber() {
		FactoryNextNumberResponse nextCustomerNumber = factoryClient.getNextCustomerNumber();
		return nextCustomerNumber.objects();
	}
	
	
	public void createOrderForRfp(String orderNumber, SevDeskContact contact, SevDeskOrderRequestForProposal rfp) {
		
		var contactPerson = baseDataClient.getUsers().objects().get(0);
		
		
		var header = orderNumber +" - Angebot einer individuellen Photovoltaik-Anlage";
		var headerText = "<p>    Sehr geehrte Damen und Herren,</p><p>    vielen Dank für Ihre Anfrage. Gerne unterbreiten wir Ihnen das gewünschte freibleibende Angebot:</p>";
		var footerText = "<p>Dieses Angebot hat aufgrund der schwankenden Rohstoffpreise eine Gültigkeit von 14 Tagen.</p><p>Nach Auftragsannahme werden <br>10% der Gesamten Bruttosumme sofort fällig.<br>50 % Der Gesamtsumme bei Lieferung der Module inkl. Unterkonstruktion.<br>30% nach der Montage.<br>10 % nach Inbetriebnahme.</p><p>Die Positionen wurden mir erläutert.</p><p>Auf die von dem Netzbetreiber gestellten Anforderungen haben wir keinen Einfluss.</p><p>Dieser Vertrag kommt bei positivem Bescheid durch den Netzbetreiber zustande. Die bereits geleistete Anzahlung wird in diesem Fall komplett zurückerstattet.</p><p>Mit freundlichen Grüßen<br><br>    [%KONTAKTPERSON%]</p>";
		
		StringBuilder address = new StringBuilder();
		address.append(contact.getQualifiedName()).append("\n");
		address.append(contact.getStreetWithHnr()).append("\n");
		address.append(contact.getPostalCode()).append(" ").append(contact.getCity());
		
		OrderRequest saveOrder = new OrderRequest();
		
		//TODO property files
		Order mOrder = Order.builder()
				.orderNumber(orderNumber)
				.contact(new Contact(contact.getTechnicalId()))
				.status(100)
				.header(header)
				.headText(headerText)
				.footText(footerText)
				.address(address.toString())
				// Germany
				.addressCountry(new AddressCountry(1))
				.contactPerson(new SevUser(contactPerson.getId()))
				.taxRate(BigDecimal.valueOf(19.0))
				.taxText("")
				.taxType("default")
				.currency("EUR")
				.showNet((short)1)
				.smallSettlement(false)
				//A normal order which documents a simple estimation / proposal for an end-customer.	
				.orderType("AN")
				.build();
		
		saveOrder.setOrder(mOrder);
		
		OrderPosition mOrderPosition = new OrderPosition();
		saveOrder.getOrderPosSave().add(mOrderPosition);
		
		StringBuilder rfpSolarsystem = new StringBuilder();
		rfpSolarsystem.append("<p>").append(rfp.getHouseType()).append(" - ").append(rfp.getRoofTopType()).append("</p>");
		rfpSolarsystem.append("<p>Anlage:       ").append(rfp.getPvPower()).append("</p>");
		rfpSolarsystem.append("<p>Speicher:     ").append(rfp.isEnergieStorage() ? rfp.getEnergieStorageCapacity() : "nein").append("</p>");
		rfpSolarsystem.append("<p>Zählerkasten: ").append(rfp.isNewMeterBox()? "ja" : "nein").append("</p>");
		rfpSolarsystem.append("<p>Wallbox:      ").append(rfp.isWallBox() ? "ja" : "nein").append("</p>");
		
		mOrderPosition.setName("Solaranlage - " + contact.getQualifiedName());
		mOrderPosition.setText(rfpSolarsystem.toString());
		
		mOrderPosition.setQuantity(BigDecimal.ONE);
		mOrderPosition.setPrice(BigDecimal.ZERO);
		mOrderPosition.setTaxRate(BigDecimal.ZERO);
		mOrderPosition.setPriceGross(BigDecimal.ZERO);
		mOrderPosition.setUnity(new MeasurementUnit(1));
		mOrderPosition.setPositionNumber(0);
		mOrderPosition.setDiscount(BigDecimal.ZERO);
		mOrderPosition.setOptional((short)0);
		mOrderPosition.setPriceTax(BigDecimal.ZERO);
		
		if(StringUtils.hasText(rfp.getCustomMessage())) {
			OrderPosition mOrderPositionMessage = new OrderPosition();
			saveOrder.getOrderPosSave().add(mOrderPositionMessage);
			
			mOrderPositionMessage.setName("Nachricht - " + contact.getQualifiedName());
			mOrderPositionMessage.setText(rfp.getCustomMessage());
			mOrderPositionMessage.setQuantity(BigDecimal.ONE);
			mOrderPositionMessage.setPrice(BigDecimal.ZERO);
			mOrderPositionMessage.setTaxRate(BigDecimal.ZERO);
			mOrderPositionMessage.setPriceGross(BigDecimal.ZERO);
			mOrderPositionMessage.setUnity(new MeasurementUnit(1));
			mOrderPositionMessage.setPositionNumber(0);
			mOrderPositionMessage.setDiscount(BigDecimal.ZERO);
			mOrderPositionMessage.setOptional((short)0);
			mOrderPositionMessage.setPriceTax(BigDecimal.ZERO);
		}
		
		orderClient.createOrder(saveOrder);
	}
	
	public void createPackageOrder() {
		
	}

}
