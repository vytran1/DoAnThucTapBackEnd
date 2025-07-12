package com.thuctap.product.dto;

import java.util.List;

import com.thuctap.utility.PageDTO;

public class ProductFindAllDTOList {
	
	private List<ProductFindAllDTO> products;
	private PageDTO page;
	public ProductFindAllDTOList() {
		super();
		// TODO Auto-generated constructor stub
	}
	public List<ProductFindAllDTO> getProducts() {
		return products;
	}
	public void setProducts(List<ProductFindAllDTO> products) {
		this.products = products;
	}
	public PageDTO getPage() {
		return page;
	}
	public void setPage(PageDTO page) {
		this.page = page;
	}
	
	
	
	
	
}
