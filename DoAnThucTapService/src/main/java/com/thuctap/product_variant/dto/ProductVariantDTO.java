package com.thuctap.product_variant.dto;

public class ProductVariantDTO {
	
	private String name;
	private String sku;
	
	
	public ProductVariantDTO() {
		super();
		// TODO Auto-generated constructor stub
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


	@Override
	public String toString() {
		return "ProductVariantDTO [name=" + name + ", sku=" + sku + "]";
	}
	
	
	
}
