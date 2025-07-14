package com.thuctap.product.dto;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.thuctap.product_attribute.dto.ProductAttributesDTO;
import com.thuctap.product_variant.dto.ProductVariantDTO;

public class ProductSaveInformationDTO {
		
	@JsonProperty("product_name")
	private String productName;
	
	private Integer brand;
	private Integer category;
	@JsonProperty("base_price")
	private BigDecimal basePrice;
	
	@JsonProperty("product_attributes")
	private List<ProductAttributesDTO> attributes;
	
	@JsonProperty("product_variants")
	private List<ProductVariantDTO> variants;

	public ProductSaveInformationDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Integer getBrand() {
		return brand;
	}

	public void setBrand(Integer brand) {
		this.brand = brand;
	}

	public Integer getCategory() {
		return category;
	}

	public void setCategory(Integer category) {
		this.category = category;
	}

	public List<ProductAttributesDTO> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<ProductAttributesDTO> attributes) {
		this.attributes = attributes;
	}

	public List<ProductVariantDTO> getVariants() {
		return variants;
	}

	public void setVariants(List<ProductVariantDTO> variants) {
		this.variants = variants;
	}
	
	

	public BigDecimal getBasePrice() {
		return basePrice;
	}

	public void setBasePrice(BigDecimal basePrice) {
		this.basePrice = basePrice;
	}

	@Override
	public String toString() {
		return "ProductSaveInformationDTO [productName=" + productName + ", brand=" + brand + ", category=" + category
				+ ", attributes=" + attributes + ", variants=" + variants + "]";
	}

	
	
	
	
	
}
