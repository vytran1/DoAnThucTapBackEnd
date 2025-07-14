package com.thuctap.product_variant.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProductVariantDetailDTO {
	private String name;
	private String sku;
	private BigDecimal price;
	@JsonProperty("default")
	private Boolean isDefault;
	
	
	

	public ProductVariantDetailDTO(String name, String sku, BigDecimal price, Boolean isDefault) {
		super();
		this.name = name;
		this.sku = sku;
		this.price = price;
		this.isDefault = isDefault;
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


	public BigDecimal getPrice() {
		return price;
	}


	public void setPrice(BigDecimal price) {
		this.price = price;
	}


	public Boolean getIsDefault() {
		return isDefault;
	}


	public void setIsDefault(Boolean isDefault) {
		this.isDefault = isDefault;
	}
	
	
	
}
