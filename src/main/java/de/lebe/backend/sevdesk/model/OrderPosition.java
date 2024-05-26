package de.lebe.backend.sevdesk.model;

import java.math.BigDecimal;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
public class OrderPosition {
	
	private final String objectName = "OrderPos";
	
	private final boolean mapAll = true;
	
    /**
     * The order to which the position belongs.
     */
    private Order order;

    /**
     * Part from your inventory which is used in the position.
     */
    private InventoryPart part;

    /**
     * Quantity of the article/part.
     */
    private BigDecimal quantity;

    /**
     * Price of the article/part. Is either gross or net, depending on the sevDesk account setting.
     */
    private BigDecimal price;

    /**
     * Tax on the price of the part.
     */
    private BigDecimal priceTax;

    /**
     * Gross price of the part.
     */
    private BigDecimal priceGross;

    /**
     * Name of the article/part.
     */
    private String name;

    /**
     * The unit in which the positions part is measured.
     */
    private MeasurementUnit unity;

    /**
     * Position number of your position. Can be used to order multiple positions.
     */
    private Integer positionNumber;

    /**
     * A text describing your position.
     */
    private String text;

    /**
     * An optional discount of the position.
     */
    private BigDecimal discount;

    /**
     * Defines if the position is optional.
     */
    private short optional;

    /**
     * Tax rate of the position.
     */
    private BigDecimal taxRate;

    /**
     * Represents the order entity.
     */
    @Data
    public static class Order {
    	
    	public Order() {
		}
		
		public Order(int id) {
			this.id = id;
		}
    	
    	/**
		 * Unique identifier of the key.
		 */
		private int id;
		private String objectName = "Order";
    }

    /**
     * Represents the inventory part entity.
     */
    @Data
    public static class InventoryPart {
    	
    	public InventoryPart() {
		}
		
		public InventoryPart(int id) {
			this.id = id;
		}
    	
    	/**
		 * Unique identifier of the key.
		 */
		private int id;
		private String objectName = "Part";
    }

    /**
     * Represents the unit entity.
     */
    @Data
    public static class MeasurementUnit {
    	public MeasurementUnit() {
		}
		
		public MeasurementUnit(int id) {
			this.id = id;
		}
    	
    	/**
		 * Unique identifier of the key.
		 */
		private int id;
		private String objectName = "Unity";
    }
}

