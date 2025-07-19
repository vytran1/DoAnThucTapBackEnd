package com.thuctap.inventory_order;

import org.springframework.data.jpa.repository.JpaRepository;

import com.thuctap.common.inventory_order.InventoryOrder;

public interface InventoryOrderRepository extends JpaRepository<InventoryOrder,Integer> {

}
