package de.lebe.backend.process;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Controller;

import com.azure.cosmos.models.PartitionKey;

import de.lebe.backend.api.dto.CustomerProposalRequest;
import de.lebe.backend.bing.AddressBingService;
import de.lebe.backend.domain.MCustomerRfp;
import de.lebe.backend.domain.MSubmissionResult;
import de.lebe.backend.domain.RepositoryCustomerRfp;
import de.lebe.backend.graphql.MailService;
import de.lebe.backend.mapper.MCustomerRfpMapper;
import de.lebe.backend.sevdesk.SevDeskContactNatPerson;
import de.lebe.backend.sevdesk.SevDeskContactService;
import de.lebe.backend.sevdesk.SevDeskOrderPlacementService;
import de.lebe.backend.sevdesk.SevDeskOrderRequestForProposal;

@Controller
public class ProcessSimpleCustomerRfp {

	@Autowired
	private MailService mailService;
	
	@Autowired
	private RepositoryCustomerRfp rpfRepository;
	
	@Autowired
	private AddressBingService bingService;
	
	@Autowired
	private SevDeskContactService contactService;
	
	@Autowired
	private SevDeskOrderPlacementService orderService;
	
	@Autowired
	private ThreadPoolTaskScheduler scheduler;
	
	
	public void processRfp(CustomerProposalRequest rfp) {
		
		var address = bingService.formatAddress(rfp.streetWithHnr(), rfp.city(), rfp.postalCode());
		
		if(!address.isQualified() || !address.getCountryRegion().equals("Germany")) {
			throw new IllegalArgumentException("Die Addresse konnte nicht gefunden werden.");
		}
		
		// First we store the rfp in the cosmos database
		MCustomerRfp mRfP = MCustomerRfpMapper.mapFromCustomerRequest(rfp, address);
		
//		rpfRepository.save(mRfP);
//		
//		scheduler.schedule(new Runnable() {
//			
//			@Override
//			public void run() {
//				// Second we send out an email to the LeBe advisor
//				mailService.sendRfpMailToBusiness(mRfP, address.getImage());
//			}
//		}, Instant.now());
		
	}
	
	public void rejectCustomerRfp(String identificationToken) {
		String id = new String(Base64.getDecoder().decode(identificationToken));
				
		rpfRepository.deleteAll(rpfRepository.findAll(new PartitionKey(id)));
	}
	
	
	public MSubmissionResult approveCustomerRfp(String identificationToken) {
		
		String id = new String(Base64.getDecoder().decode(identificationToken));
		
		List<MCustomerRfp> matches = new ArrayList<MCustomerRfp>();
		rpfRepository.findAll(new PartitionKey(id)).forEach(matches::add);
		
		var lastSubmittion = matches.stream().max((a, b) -> a.getSubmittionDate().compareTo(b.getSubmittionDate()));
		
		if(lastSubmittion.isEmpty()) {
			throw new IllegalArgumentException("Die Freigabe ist bereits abgelaufen!");
		}
		var customer = lastSubmittion.get().getCustomer();
		
		String orderNumber = orderService.getOrderNumber();
		
		SevDeskContactNatPerson sevDeskContact = new SevDeskContactNatPerson();
		sevDeskContact.setGender("M");
		sevDeskContact.setFirstname(customer.getName());
		sevDeskContact.setLastname(customer.getLastname());
		sevDeskContact.setStreetWithHnr(customer.getStreetWithHnr());
		sevDeskContact.setPostalCode(customer.getPostalCode());
		sevDeskContact.setCity(customer.getCity());
		sevDeskContact.setEmail(customer.getEmail());
		sevDeskContact.setMobile(customer.getMobileNumber());
		
		contactService.createContact(sevDeskContact);
		
		
		var solar = lastSubmittion.get().getSolarSystem();
		
		SevDeskOrderRequestForProposal rfp = new SevDeskOrderRequestForProposal();
		rfp.setCustomMessage(orderNumber);
		rfp.setDate(lastSubmittion.get().getSubmittionDate().toLocalDate());
		rfp.setRoofTopType(lastSubmittion.get().getHouseDetails().getRoofTopType());
		rfp.setHouseType(lastSubmittion.get().getHouseDetails().getHouseType());
		
		rfp.setEnergieStorage(solar.isEnergieStorage());
		rfp.setEnergieStorageCapacity(solar.getEnergieStorageCapacity());
		rfp.setNewMeterBox(solar.isNewMeterBox());
		rfp.setPvPower(solar.getPvPower());
		rfp.setWallBox(solar.isWallBox());;;
		
		orderService.createOrderForRfp(orderNumber, sevDeskContact, rfp);
		
		return new MSubmissionResult(lastSubmittion.get(), sevDeskContact.getCustomerNumber(), orderNumber);
	}

	
}
