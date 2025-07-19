package com.thuctap.inventory_order.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

public class InventoryOrderDetailWithExpectedPriceDTO {
	private String sku;
	private String name;
	private Integer quantity;
	@JsonProperty("expected_price")
	private BigDecimal expectedPrice;
	public InventoryOrderDetailWithExpectedPriceDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		this.sku = sku;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public BigDecimal getExpectedPrice() {
		return expectedPrice;
	}
	public void setExpectedPrice(BigDecimal expectedPrice) {
		this.expectedPrice = expectedPrice;
	}
	@Override
	public String toString() {
		return "InventoryOrderDetailWithExpectedPriceDTO [sku=" + sku + ", name=" + name + ", quantity=" + quantity
				+ ", expectedPrice=" + expectedPrice + "]";
	}
	
	
}
