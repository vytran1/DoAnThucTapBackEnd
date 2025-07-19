package com.thuctap.product_variant.dto;

import java.util.List;

import com.thuctap.utility.PageDTO;

public class ProductVariantForTransactionAggregator {
	
	private List<ProductVariantForTransactionDTO> variants;
	private PageDTO page;
	
	public ProductVariantForTransactionAggregator() {
		super();
		// TODO Auto-generated constructor stub
	}

	public List<ProductVariantForTransactionDTO> getVariants() {
		return variants;
	}

	public void setVariants(List<ProductVariantForTransactionDTO> variants) {
		this.variants = variants;
	}

	public PageDTO getPage() {
		return page;
	}

	public void setPage(PageDTO page) {
		this.page = page;
	}
	
	
	

}
