package com.thuctap.inventory_order.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class InventoryOrderSavingRequestAggregatorDTO {
	private Integer supplier;
	@JsonProperty("order_details")
	private List<InventoryOrderDetailWithExpectedPriceDTO> orderDetails;
	public InventoryOrderSavingRequestAggregatorDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Integer getSupplier() {
		return supplier;
	}
	public void setSupplier(Integer supplier) {
		this.supplier = supplier;
	}
	public List<InventoryOrderDetailWithExpectedPriceDTO> getOrderDetails() {
		return orderDetails;
	}
	public void setOrderDetails(List<InventoryOrderDetailWithExpectedPriceDTO> orderDetails) {
		this.orderDetails = orderDetails;
	}
	@Override
	public String toString() {
		return "InventoryOrderSavingRequestAggregatorDTO [supplier=" + supplier + ", orderDetails=" + orderDetails
				+ "]";
	}
	
	
}
