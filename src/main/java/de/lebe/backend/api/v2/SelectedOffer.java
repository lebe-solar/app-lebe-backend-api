package de.lebe.backend.api.v2;

import java.util.List;

public record SelectedOffer(
	    int id,
	    String title,
	    String subtitle,
	    String description,
	    String conditions,
	    String validUntil,
	    String designedFor,
	    String pvModules,
	    String inverter,
	    String energyStorage,
	    String price,
	    String link,
	    String previewImage,
	    List<Integer> productIds,
	    ProductDetails products,
	    List<InclusiveItem> inclusive,
	    boolean allowChanges
	) {}
