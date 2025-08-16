package com.thuctap.inventory_order;

public class InventoryOrderCreatedEvent {
	
	private Integer inventoryOrderId;
	
	public InventoryOrderCreatedEvent(Integer inventoryOrderId) {
		super();
		this.inventoryOrderId = inventoryOrderId;
	}

	public Integer getInventoryOrderId() {
		return inventoryOrderId;
	}

	public void setInventoryOrderId(Integer inventoryOrderId) {
		this.inventoryOrderId = inventoryOrderId;
	}
	
	
	
}
