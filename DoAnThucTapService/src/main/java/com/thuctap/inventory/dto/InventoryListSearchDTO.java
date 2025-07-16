package com.thuctap.inventory.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class InventoryListSearchDTO {
	
	private Integer id;
	@JsonProperty("inventory_code")
	private String inventoryCode;
	
	
	public InventoryListSearchDTO(Integer id, String inventoryCode) {
		super();
		this.id = id;
		this.inventoryCode = inventoryCode;
	}


	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public String getInventoryCode() {
		return inventoryCode;
	}


	public void setInventoryCode(String inventoryCode) {
		this.inventoryCode = inventoryCode;
	}
	
	
	
	
}
