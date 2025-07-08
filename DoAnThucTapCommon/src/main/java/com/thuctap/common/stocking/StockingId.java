package com.thuctap.common.stocking;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class StockingId implements Serializable {
	
	@Column(name = "inventory_id")
	private Integer inventoryId;
	
	@Column(name = "sku", length = 125)
    private String sku;
    
	public StockingId(Integer inventoryId, String sku) {
		super();
		this.inventoryId = inventoryId;
		this.sku = sku;
	}

	@Override
	public int hashCode() {
		return Objects.hash(inventoryId, sku);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		StockingId other = (StockingId) obj;
		return Objects.equals(inventoryId, other.inventoryId) && Objects.equals(sku, other.sku);
	}

	public Integer getInventoryId() {
		return inventoryId;
	}

	public void setInventoryId(Integer inventoryId) {
		this.inventoryId = inventoryId;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}
	
	
    
    
	
}
