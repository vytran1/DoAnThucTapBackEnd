package com.thuctap.product.dto;

import java.util.List;

import com.thuctap.product_image.dto.ProductImageDTO;

public class ProductImageAggregatorDTO {
	private String mainImage;
	private List<ProductImageDTO> subImages;
	public ProductImageAggregatorDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getMainImage() {
		return mainImage;
	}
	public void setMainImage(String mainImage) {
		this.mainImage = mainImage;
	}
	public List<ProductImageDTO> getSubImages() {
		return subImages;
	}
	public void setSubImages(List<ProductImageDTO> subImages) {
		this.subImages = subImages;
	}
	
	
	
}
