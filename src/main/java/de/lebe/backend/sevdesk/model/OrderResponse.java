package de.lebe.backend.sevdesk.model;

import java.util.List;

import lombok.Data;

@Data
public class OrderResponse {

	private Order order;
	
	private List<Object> orderPos;
}
