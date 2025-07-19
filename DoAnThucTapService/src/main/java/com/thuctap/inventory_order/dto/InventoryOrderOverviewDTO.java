package com.thuctap.inventory_order.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class InventoryOrderOverviewDTO {
	@JsonProperty("order_code")
	private String orderCode;
	
	private String employee;
	
	@JsonProperty("current_status")
	private String currentStatus;
	
	@JsonProperty("created_at")
	private String createdAt;
	
	@JsonProperty("completed_at")
	private String completedAt;
	
	@JsonProperty("line_items")
	private Integer lineItems;
	
	@JsonProperty("order_details")
	private List<InventoryOrderDetailForOverviewDTO> orderDetails;
	
	

	public InventoryOrderOverviewDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public String getEmployee() {
		return employee;
	}

	public void setEmployee(String employee) {
		this.employee = employee;
	}

	public String getCurrentStatus() {
		return currentStatus;
	}

	public void setCurrentStatus(String currentStatus) {
		this.currentStatus = currentStatus;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public String getCompletedAt() {
		return completedAt;
	}

	public void setCompletedAt(String completedAt) {
		this.completedAt = completedAt;
	}

	public List<InventoryOrderDetailForOverviewDTO> getOrderDetails() {
		return orderDetails;
	}

	public void setOrderDetails(List<InventoryOrderDetailForOverviewDTO> orderDetails) {
		this.orderDetails = orderDetails;
	}

	public Integer getLineItems() {
		return lineItems;
	}

	public void setLineItems(Integer lineItems) {
		this.lineItems = lineItems;
	}
	
	
	
	
}
