package com.thuctap.common.inventory_order;

import java.io.Serializable;
import java.util.Objects;

public class InventoryOrderStatusId implements Serializable {
	private Integer order;
    private Integer status;
	public InventoryOrderStatusId() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	public InventoryOrderStatusId(Integer order, Integer status) {
		super();
		this.order = order;
		this.status = status;
	}


	@Override
	public int hashCode() {
		return Objects.hash(order, status);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		InventoryOrderStatusId other = (InventoryOrderStatusId) obj;
		return Objects.equals(order, other.order) && Objects.equals(status, other.status);
	}
    
    
}
