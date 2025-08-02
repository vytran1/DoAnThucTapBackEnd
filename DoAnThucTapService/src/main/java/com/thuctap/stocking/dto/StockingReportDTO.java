package com.thuctap.stocking.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StockingReportDTO {
	private String name;
	private String sku;
	private Long quantity;
	private BigDecimal values;
	@JsonProperty("low_stock")
	private boolean lowStock;
	public StockingReportDTO(String name, String sku, Long quantity, BigDecimal values) {
		super();
		this.name = name;
		this.sku = sku;
		this.quantity = quantity;
		this.values = values;
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
	public Long getQuantity() {
		return quantity;
	}
	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}
	public BigDecimal getValues() {
		return values;
	}
	public void setValues(BigDecimal values) {
		this.values = values;
	}
	public boolean isLowStock() {
		return lowStock;
	}
	public void setLowStock(boolean lowStock) {
		this.lowStock = lowStock;
	}
	
	
	
	
	
	
}
