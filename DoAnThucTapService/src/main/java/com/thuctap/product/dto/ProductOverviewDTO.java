package com.thuctap.product.dto;

import java.math.BigDecimal;

public class ProductOverviewDTO {
	private String name;
	private String sku;
	private String brand;
	private String category;
	private BigDecimal price;
	
	
	public ProductOverviewDTO(String name, String sku, String brand, String category, BigDecimal price) {
		super();
		this.name = name;
		this.sku = sku;
		this.brand = brand;
		this.category = category;
		this.price = price;
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


	public String getBrand() {
		return brand;
	}


	public void setBrand(String brand) {
		this.brand = brand;
	}


	public String getCategory() {
		return category;
	}


	public void setCategory(String category) {
		this.category = category;
	}


	public BigDecimal getPrice() {
		return price;
	}


	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	
	
	
}
