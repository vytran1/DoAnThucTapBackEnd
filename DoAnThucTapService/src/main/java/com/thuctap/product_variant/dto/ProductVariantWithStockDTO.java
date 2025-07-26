package com.thuctap.product_variant.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProductVariantWithStockDTO {
	private Integer id;
	private Integer parentId;
	private String name;
	private String sku;
	private String image;
	@JsonProperty("current_quantity")
	private Long currentQuantity;
	@JsonProperty("cost_price")
	private BigDecimal costPrice;
	public ProductVariantWithStockDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
	
	public ProductVariantWithStockDTO(Integer id, String name, String sku, String image, Long currentQuantity,
			BigDecimal costPrice) {
		super();
		this.id = id;
		this.name = name;
		this.sku = sku;
		this.image = image;
		this.currentQuantity = currentQuantity;
		this.costPrice = costPrice;
	}

	


	public ProductVariantWithStockDTO(Integer id, Integer parentId, String name, String sku, String image,
			Long currentQuantity, BigDecimal costPrice) {
		super();
		this.id = id;
		this.parentId = parentId;
		this.name = name;
		this.sku = sku;
		this.image = image;
		this.currentQuantity = currentQuantity;
		this.costPrice = costPrice;
	}


	

	public Integer getParentId() {
		return parentId;
	}




	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}




	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		this.sku = sku;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public Long getCurrentQuantity() {
		return currentQuantity;
	}
	public void setCurrentQuantity(Long currentQuantity) {
		this.currentQuantity = currentQuantity;
	}
	public BigDecimal getCostPrice() {
		return costPrice;
	}
	public void setCostPrice(BigDecimal costPrice) {
		this.costPrice = costPrice;
	}
	
	
}
