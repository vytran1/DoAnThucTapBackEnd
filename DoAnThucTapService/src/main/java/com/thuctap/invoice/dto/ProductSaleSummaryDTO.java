package com.thuctap.invoice.dto;

public class ProductSaleSummaryDTO {
	private String sku;
	private Long totalQuantity;
	public ProductSaleSummaryDTO(String sku, Long totalQuantity) {
		super();
		this.sku = sku;
		this.totalQuantity = totalQuantity;
	}
	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		this.sku = sku;
	}
	public Long getTotalQuantity() {
		return totalQuantity;
	}
	public void setTotalQuantity(Long totalQuantity) {
		this.totalQuantity = totalQuantity;
	}
	
	
}
