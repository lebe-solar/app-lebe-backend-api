package de.lebe.backend.bing;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MAddressFormatted {
	
	private boolean qualified;

	private String addressLine;
	private String adminDistrict;
	private String adminDistrict2;
	private String countryRegion;
	private String formattedAddress;
	private String locality;
	private String postalCode;
    
    private byte[] image;
}
