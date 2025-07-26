package com.thuctap.product_variant.dto;

import java.util.List;

import com.thuctap.utility.PageDTO;

public class ProductVariantWithStockAggregator {
	private List<ProductVariantWithStockDTO> variants;
	private PageDTO page;
	public ProductVariantWithStockAggregator() {
		super();
		// TODO Auto-generated constructor stub
	}
	public List<ProductVariantWithStockDTO> getVariants() {
		return variants;
	}
	public void setVariants(List<ProductVariantWithStockDTO> variants) {
		this.variants = variants;
	}
	public PageDTO getPage() {
		return page;
	}
	public void setPage(PageDTO page) {
		this.page = page;
	}
	
	
	
	
}
