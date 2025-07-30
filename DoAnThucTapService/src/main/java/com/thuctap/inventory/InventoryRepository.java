package com.thuctap.inventory;


import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.thuctap.common.inventory.Inventory;

public interface InventoryRepository extends JpaRepository<Inventory, Integer> {
	
	@Query("SELECT iv  FROM Inventory iv JOIN FETCH iv.district WHERE iv.isDelete = false")
	public Page<Inventory> findAllNotDeleted(Pageable pageable);
	
	@Query("SELECT i FROM Inventory i WHERE i.isDelete = false AND (" +
	           "LOWER(i.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
	           "LOWER(i.inventoryCode) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
	           "LOWER(i.address) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
	           "LOWER(i.district.name) LIKE LOWER(CONCAT('%', :keyword, '%')))")
	    Page<Inventory> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);

	public Optional<Inventory> findByInventoryCode(String inventoryCode);
	
	public Optional<Inventory> findByInventoryCodeAndIdNot(String inventoryCode, Integer id);
}
