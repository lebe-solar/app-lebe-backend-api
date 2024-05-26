package de.lebe.backend.sevdesk.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class OrderRequest {

	private Order order;
	
	private List<OrderPosition> orderPosSave = new ArrayList<>();
}
