package com.thuctap.product_variant.dto;

public class ProductVariantForTransactionDTO {

	String sku;
	String name;
	
	
	
	public ProductVariantForTransactionDTO(String sku, String name) {
		super();
		this.sku = sku;
		this.name = name;
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
	
	
	
}
