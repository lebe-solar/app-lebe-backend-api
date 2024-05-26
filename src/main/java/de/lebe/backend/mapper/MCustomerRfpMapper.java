package de.lebe.backend.mapper;

import java.time.LocalDateTime;
import java.util.Base64;
import java.util.UUID;

import de.lebe.backend.api.dto.CustomerProposalRequest;
import de.lebe.backend.bing.MAddressFormatted;
import de.lebe.backend.domain.MCustomerConsumption;
import de.lebe.backend.domain.MCustomerInput;
import de.lebe.backend.domain.MCustomerRfp;
import de.lebe.backend.domain.MHouseDetails;
import de.lebe.backend.domain.MPrivateCustomer;
import de.lebe.backend.domain.MSolarSystem;

public abstract class MCustomerRfpMapper {

	public static MCustomerRfp mapFromCustomerRequest(CustomerProposalRequest rfp, MAddressFormatted address) {
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
		
		mRfP.setSolarSystem(new MSolarSystem());
		mRfP.getSolarSystem().setEnergieStorage(rfp.energieStorage());
		mRfP.getSolarSystem().setNewMeterBox(rfp.newMeterBox());
		mRfP.getSolarSystem().setWallBox(rfp.wallBox());
		mRfP.getSolarSystem().setPvPower(rfp.pvPower());
		mRfP.getSolarSystem().setEnergieStorageCapacity(rfp.energieStorageCapacity());
		
		mRfP.setCustomerConsumption(new MCustomerConsumption());
		mRfP.getCustomerConsumption().setEnergy(rfp.energieUsage());
		return mRfP;
	}
	
	public static String getPartionKey(MCustomerRfp rfp) {
		return Base64.getEncoder().encodeToString(rfp.getCustomerEmail().getBytes());
	}
}
