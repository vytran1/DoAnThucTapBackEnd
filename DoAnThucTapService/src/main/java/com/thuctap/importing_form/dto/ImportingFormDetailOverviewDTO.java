package com.thuctap.importing_form.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ImportingFormDetailOverviewDTO {
	
	private String sku;
	private Integer quantity;
	@JsonProperty("cost_price")
	private BigDecimal costPrice;
	@JsonProperty("total_value")
	private BigDecimal totalValue;
	public ImportingFormDetailOverviewDTO(String sku, Integer quantity, BigDecimal costPrice, BigDecimal totalValue) {
		super();
		this.sku = sku;
		this.quantity = quantity;
		this.costPrice = costPrice;
		this.totalValue = totalValue;
	}
	public ImportingFormDetailOverviewDTO() {
		super();
		// TODO Auto-generated constructor stub
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
	public BigDecimal getTotalValue() {
		return totalValue;
	}
	public void setTotalValue(BigDecimal totalValue) {
		this.totalValue = totalValue;
	}
	
	
	
}
