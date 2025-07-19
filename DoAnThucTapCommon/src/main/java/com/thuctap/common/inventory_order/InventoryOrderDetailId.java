package com.thuctap.common.inventory_order;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class InventoryOrderDetailId implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name = "order_id")
    private Integer orderId;

    @Column(name = "sku")
    private String sku;

    public InventoryOrderDetailId() {}

    public InventoryOrderDetailId(Integer orderId, String sku) {
        this.orderId = orderId;
        this.sku = sku;
    }

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	@Override
	public int hashCode() {
		return Objects.hash(orderId, sku);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		InventoryOrderDetailId other = (InventoryOrderDetailId) obj;
		return Objects.equals(orderId, other.orderId) && Objects.equals(sku, other.sku);
	}
    
    
}
