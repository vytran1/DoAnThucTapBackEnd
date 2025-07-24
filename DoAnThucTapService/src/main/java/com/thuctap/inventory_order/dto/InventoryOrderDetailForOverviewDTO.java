package com.thuctap.inventory_order.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

public class InventoryOrderDetailForOverviewDTO {
	private String name;
	private String sku;
	private Integer quantity; 
	
	@JsonProperty("cost_price")
	private BigDecimal costPrice;
	
	@JsonProperty("quote_price")
	private BigDecimal quotePrice;
	
	@JsonProperty("expected_price")
	private BigDecimal expectedPrice;
	
	

	public InventoryOrderDetailForOverviewDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	

	public InventoryOrderDetailForOverviewDTO(String name, String sku, Integer quantity, BigDecimal costPrice,
			BigDecimal quotePrice, BigDecimal expectedPrice) {
		super();
		this.name = name;
		this.sku = sku;
		this.quantity = quantity;
		this.costPrice = costPrice;
		this.quotePrice = quotePrice;
		this.expectedPrice = expectedPrice;
	}



	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getCostPrice() {
		return costPrice;
	}

	public void setCostPrice(BigDecimal costPrice) {
		this.costPrice = costPrice;
	}

	public BigDecimal getQuotePrice() {
		return quotePrice;
	}

	public void setQuotePrice(BigDecimal quotePrice) {
		this.quotePrice = quotePrice;
	}

	public BigDecimal getExpectedPrice() {
		return expectedPrice;
	}

	public void setExpectedPrice(BigDecimal expectedPrice) {
		this.expectedPrice = expectedPrice;
	}
	
	
}
