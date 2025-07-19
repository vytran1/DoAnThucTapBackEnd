package com.thuctap.inventory_order.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class InventoryOrderStatusDTO {
	
	private String updater;
	private String status;
	private String description;
	@JsonProperty("created_at")
	private String createdAt;
	public InventoryOrderStatusDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getUpdater() {
		return updater;
	}
	public void setUpdater(String updater) {
		this.updater = updater;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}
	
	
}
