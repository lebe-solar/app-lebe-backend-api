package de.lebe.backend.api.v2;

public record PvSystem(
	    Module module,
	    Inverter inverter,
	    Battery battery,
	    int module_quantity,
	    int inverter_quantity,
	    int battery_quantity,
	    Offer offer,
	    SelectedOffer selectedOffer
	) {}