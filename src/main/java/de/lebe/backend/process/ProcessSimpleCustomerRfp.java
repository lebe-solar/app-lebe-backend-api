package de.lebe.backend.process;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import de.lebe.backend.api.dto.CustomerProposalRequest;
import de.lebe.backend.bing.AddressBingService;
import de.lebe.backend.domain.MCustomerConsumption;
import de.lebe.backend.domain.MCustomerInput;
import de.lebe.backend.domain.MCustomerRfp;
import de.lebe.backend.domain.MHouseDetails;
import de.lebe.backend.domain.MPrivateCustomer;
import de.lebe.backend.domain.MSolarSystem;
import de.lebe.backend.domain.RepositoryCustomerRfp;
import de.lebe.backend.graphql.MailService;

@Controller
public class ProcessSimpleCustomerRfp {

	@Autowired
	private MailService mailService;
	
	@Autowired
	private RepositoryCustomerRfp rpfRepository;
	
	@Autowired
	private AddressBingService bingService;
	
	public void processRfp(CustomerProposalRequest rfp) {
		
		var address = bingService.formatAddress(rfp.streetWithHnr(), rfp.city(), rfp.postalCode());
		
		if(!address.isQualified() || !address.getCountryRegion().equals("Germany")) {
			throw new IllegalArgumentException("Die Addresse konnte nicht gefunden werden.");
		}
		
		
		// First we store the rfp in the cosmos database
		MCustomerRfp mRfP = new MCustomerRfp();
		mRfP.setTechnicalId(UUID.randomUUID().toString());
		mRfP.setSubmittionDate(LocalDateTime.now());
		mRfP.setCustomerEmail(rfp.email());
		
		mRfP.setCustomer(new MPrivateCustomer());
		mRfP.getCustomer().setCity(address.getLocality());
		mRfP.getCustomer().setPostalCode(address.getPostalCode());
		mRfP.getCustomer().setStreetWithHnr(address.getAddressLine());
		
		mRfP.getCustomer().setName(rfp.firstname());
		mRfP.getCustomer().setLastname(rfp.lastname());
		mRfP.getCustomer().setEmail(rfp.email());
		mRfP.getCustomer().setMobileNumber(rfp.mobile());
		
		mRfP.setHouseDetails(new MHouseDetails());
		mRfP.getHouseDetails().setHouseType(rfp.houseType());
		mRfP.getHouseDetails().setRoofTopType(rfp.roofTopType());
		
		
		mRfP.setCustomerInput(new MCustomerInput());
		mRfP.getCustomerInput().setMessage(rfp.customMessage());
		
		// Image and address by Bing
		
		
		mRfP.setSolarSystem(new MSolarSystem());
		mRfP.getSolarSystem().setEnergieStorage(rfp.energieStorage());
		mRfP.getSolarSystem().setNewMeterBox(rfp.newMeterBox());
		mRfP.getSolarSystem().setWallBox(rfp.wallBox());
		mRfP.getSolarSystem().setPvPower(rfp.pvPower());
		mRfP.getSolarSystem().setEnergieStorageCapacity(rfp.energieStorageCapacity());
		
		mRfP.setCustomerConsumption(new MCustomerConsumption());
		mRfP.getCustomerConsumption().setEnergy(rfp.energieUsage());
		
		rpfRepository.save(mRfP);
		
		// Second we send out an email to the LeBe advisor
		mailService.sendRfpMailToBusiness(mRfP, address.getImage());
	}
}
