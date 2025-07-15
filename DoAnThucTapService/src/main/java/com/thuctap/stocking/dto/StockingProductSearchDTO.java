package com.thuctap.stocking.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StockingProductSearchDTO {
	@JsonProperty("inventory_code")
	private String inventoryCode;
	
	
	private Long quantity;


	public StockingProductSearchDTO(String inventoryCode, Long quantity) {
		super();
		this.inventoryCode = inventoryCode;
		this.quantity = quantity;
	}


	public String getInventoryCode() {
		return inventoryCode;
	}


	public void setInventoryCode(String inventoryCode) {
		this.inventoryCode = inventoryCode;
	}


	public Long getQuantity() {
		return quantity;
	}


	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}
	
	
}
