package com.thuctap.inventory_order;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.thuctap.common.inventory_order.InventoryOrder;

public interface InventoryOrderRepository extends JpaRepository<InventoryOrder,Integer> {
	
	
	
	@Query("UPDATE InventoryOrder io SET io.quoteShippingFee = ?1 WHERE io.id = ?2")
	@Modifying
	public void updateShippingFee(BigDecimal shippingFee,Integer orderId);
	
}
