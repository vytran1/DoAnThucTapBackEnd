package com.thuctap.inventory;

import org.springframework.data.jpa.repository.JpaRepository;

import com.thuctap.common.inventory.Inventory;

public interface InventoryRepository extends JpaRepository<Inventory, Integer> {

}
