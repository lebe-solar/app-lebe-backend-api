package de.lebe.backend.sevdesk.model;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@AllArgsConstructor
@lombok.Builder
@Data
@EqualsAndHashCode
public class Order extends BaseModel {
	
	private final String objectName = "Order";
	
	private final boolean mapAll = true;
	
	public Order() {
	}
	
	/**
	 * Defines the vat-regulation for the order. Can be: default, eu, noteu, custom,
	 * ss
	 */
	@Builder.Default
	private String taxType = "default";

	/**
	 * Your own tax set that should be used if you defined custom as taxType. Nested
	 * object (TaxSet)
	 */
	private TaxSet taxSet;

	/**
	 * Currency of the order. Needs to be currency code according to ISO-4217.
	 */
	private String currency;

	/**
	 * Version of the order. Can be used if you have multiple drafts for the same
	 * order. Should start with 0.
	 */
	@Builder.Default
	private int version = 0;

	/**
	 * Text describing the VAT regulation you chose. A common text of our German
	 * customers would be: Umsatzsteuer ausweisen or zzgl. Umsatzsteuer 19%
	 */
	private String taxText;

	/**
	 * The tax rate of the order. Please be aware that this value will be
	 * overwritten by tax rates of the order positions.
	 */
	private BigDecimal taxRate;

	/**
	 * The sevDesk user which acts as a contact person for this order. Nested object
	 * (SevUser)
	 */
	private SevUser contactPerson;

	/**
	 * If the sevDesk account is falling under the small entrepreneur scheme, the
	 * order mustn't contain any VAT. If this is the case, this attribute should be
	 * true, otherwise false.
	 */
	private Boolean smallSettlement;

	/**
	 * The order date. Date / Timestamp
	 */
	@Builder.Default
	private ZonedDateTime orderDate = ZonedDateTime.now();

	/**
	 * The order status.
	 * <li>Draft The order is still a draft. It has not been sent to the
	 * end-customer and can still be changed. 100
	 * <li>Delivered The order has been sent to the end-customer. 200
	 * <li>Rejected / Cancelled The order has been rejected by the end-customer. 300
	 * <li>Accepted The order has been accepted by the end-customer. 500
	 * <li>Partially Calculated An invoice for parts of the order (but not the full
	 * order) has been created. 750
	 * <li>Calculated The order has been calculated. One or more invoices have been
	 * created covering the whole order. 1000
	 */
	@Builder.Default
	private Integer status = 100;

	/**
	 * This attribute determines if the price you give the order positions will be
	 * regarded as gross or net. If true, the price attribute will hold the net
	 * value, otherwise the gross value.
	 */
	private short showNet;

	/**
	 * You can use this attribute to provide a note for the order. It can be used
	 * for reference numbers, order numbers, or other important information.
	 */
	private String customerInternalNote;

	/**
	 * Holds the complete address to which the order is directed. You can use line
	 * breaks to separate the different address parts.
	 */
	private String address;

	private AddressCountry addressCountry;

	/**
	 * If you don't plan to send the order over another endpoint like
	 * /Order/sendViaEmail or Order/sendBy but instead give it the status "200"
	 * directly, you need to specify a send type here. Valid types are: VPR
	 * (printed), VPDF (downloaded), VM (mailed), VP (postal).
	 */
	private String sendType;

	/**
	 * You can specify the object from which an order originated. Just provide the
	 * ID of this object.
	 */
	private Integer origin;

	/**
	 * Specifies the object name of the object from which an order originated. Most
	 * likely Order or Invoice.
	 */
	private String typeOrigin;

	/**
	 * The order number.
	 */
	private String orderNumber;

	/**
	 * The end-customer to which the order is directed. Nested object (Contact)
	 */
	private Contact contact;

	/**
	 * The order header. Usually consists of the order number and a prefix.
	 */
	private String header;

	/**
	 * A head text for the order. Can contain certain HTML tags.
	 */
	private String headText;

	/**
	 * A foot text for the order. Can contain certain HTML tags.
	 */
	private String footText;

	/**
	 * The payment terms for the order.
	 */
	private Integer paymentTerms;

	/**
	 * The delivery terms for the order.
	 */
	private Integer deliveryTerms;

	/**
	 * The date the order was sent to the end-customer. Timestamp
	 */
	private ZonedDateTime sendDate;

	/**
	 * The order type.
	 */
	private String orderType;

	/**
	 * Represents the tax set entity.
	 */
	@Data
	public static class TaxSet {
		
		public TaxSet() {
		}
		
		public TaxSet(int id) {
			this.id = id;
		}
		
		/**
		 * Unique identifier of the key.
		 */
		private int id;
		private String objectName = "TaxSet";
	}

	/**
	 * Represents the sevDesk user entity.
	 */
	@Data
	public static class SevUser {
		
		public SevUser() {
		}
		
		public SevUser(int id) {
			this.id = id;
		}
		
		/**
		 * Unique identifier of the key.
		 */
		private int id;
		private String objectName = "SevUser";
	}

	/**
	 * Represents the contact entity.
	 */
	@Data
	public static class Contact {
		
		public Contact() {
		}
		
		public Contact(int id) {
			this.id = id;
		}
		/**
		 * Unique identifier of the key.
		 */
		private int id;
		private String objectName = "Contact";
	}

	/**
	 * Can be omitted as complete address is defined in address attribute.
	 */
	@Data
	public static class AddressCountry {
		
		public AddressCountry() {
		}
		
		public AddressCountry(int id) {
			this.id = id;
		}
		/**
		 * Unique identifier of the key.
		 */
		private int id;
		private String objectName = "StaticCountry";
	}
}
