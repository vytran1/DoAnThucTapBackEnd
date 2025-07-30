package com.thuctap.inventory;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class InventoryDTO {
	private Integer id;
	
	@NotBlank(message = "Inventory code is required and cannot be blank.")
	@Size(min = 3, max = 65, message = "Inventory code must be between 3 and 65 characters.")
	private String inventoryCode;
	
	@NotBlank(message = "Inventory name is required.")
	@Size(min = 3, max = 65, message = "Inventory name must be between 3 and 65 characters." )
	private String inventoryName;
	
	@NotBlank(message = "Address is required.")
	@Size(min = 3, max = 65, message = "Address must be between 3 and 65 characters." )
	private String address;
	
	@NotNull(message = "District is required")
	private Integer districtID;
	
	private String districtName;
	
	public InventoryDTO() {
		super();
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

	public String getInventoryName() {
		return inventoryName;
	}

	public void setInventoryName(String inventoryName) {
		this.inventoryName = inventoryName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Integer getDistrictID() {
		return districtID;
	}

	public void setDistrictID(Integer districtID) {
		this.districtID = districtID;
	}

	public String getDistrictName() {
		return districtName;
	}

	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}
	
	
}
