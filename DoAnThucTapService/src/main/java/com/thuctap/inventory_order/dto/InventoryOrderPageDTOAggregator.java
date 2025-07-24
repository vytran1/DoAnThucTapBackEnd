package com.thuctap.inventory_order.dto;

import java.util.List;

import com.thuctap.utility.PageDTO;

public class InventoryOrderPageDTOAggregator {
	
	List<InventoryOrderPageDTO> orders;
	PageDTO page;
	public InventoryOrderPageDTOAggregator() {
		super();
		// TODO Auto-generated constructor stub
	}
	public List<InventoryOrderPageDTO> getOrders() {
		return orders;
	}
	public void setOrders(List<InventoryOrderPageDTO> orders) {
		this.orders = orders;
	}
	public PageDTO getPage() {
		return page;
	}
	public void setPage(PageDTO page) {
		this.page = page;
	}
	
	
}
