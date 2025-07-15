package com.thuctap.product_variant.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProductVariantInventoryDTO {
	

	private String name;
	private String sku;
	private BigDecimal price;
	
	@JsonProperty("on_hand")
	private Long totalQuantity;

	public ProductVariantInventoryDTO(String name, String sku, BigDecimal price, Long totalQuantity) {
		super();
		this.name = name;
		this.sku = sku;
		this.price = price;
		this.totalQuantity = totalQuantity;
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

	public Long getTotalQuantity() {
		return totalQuantity;
	}

	public void setTotalQuantity(Long totalQuantity) {
		this.totalQuantity = totalQuantity;
	}
	
	
	
}
