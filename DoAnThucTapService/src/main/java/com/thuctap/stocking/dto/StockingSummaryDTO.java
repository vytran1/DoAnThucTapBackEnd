package com.thuctap.stocking.dto;

public class StockingSummaryDTO {
	private String sku;
	private String name;
	private Long totalQuantity;
	public StockingSummaryDTO(String sku, String name, Long totalQuantity) {
		super();
		this.sku = sku;
		this.name = name;
		this.totalQuantity = totalQuantity;
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
	public Long getTotalQuantity() {
		return totalQuantity;
	}
	public void setTotalQuantity(Long totalQuantity) {
		this.totalQuantity = totalQuantity;
	}
	
	
}
