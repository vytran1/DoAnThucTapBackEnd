package com.thuctap.product.dto;

import java.util.List;

import com.thuctap.product_attribute.dto.ProductAttributeForProductDetailDTO;
import com.thuctap.product_variant.dto.ProductVariantDetailDTO;

public class ProductDetailForVariantAggregateDTO {
	
	private List<ProductVariantDetailDTO> variants;
	private List<ProductAttributeForProductDetailDTO> attribute;
	
	
	public ProductDetailForVariantAggregateDTO() {
		super();
		// TODO Auto-generated constructor stub
	}


	public List<ProductVariantDetailDTO> getVariants() {
		return variants;
	}


	public void setVariants(List<ProductVariantDetailDTO> variants) {
		this.variants = variants;
	}


	public List<ProductAttributeForProductDetailDTO> getAttribute() {
		return attribute;
	}


	public void setAttribute(List<ProductAttributeForProductDetailDTO> attribute) {
		this.attribute = attribute;
	}
	
	
	
}
