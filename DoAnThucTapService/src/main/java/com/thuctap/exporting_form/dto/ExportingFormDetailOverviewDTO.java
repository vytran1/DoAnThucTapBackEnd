package com.thuctap.exporting_form.dto;

public class ExportingFormDetailOverviewDTO {
	private String sku;
	private Integer quantity;
	
	
	public ExportingFormDetailOverviewDTO(String sku, Integer quantity) {
		super();
		this.sku = sku;
		this.quantity = quantity;
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
	
	
	
}
