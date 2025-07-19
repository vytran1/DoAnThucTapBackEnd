package com.thuctap.inventory.vytran;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.thuctap.common.inventory.Inventory;
import com.thuctap.inventory.dto.InventoryListSearchDTO;

public interface VInventoryRepository extends JpaRepository<Inventory,Integer>{

	
	
	@Query("""
			SELECT
				new com.thuctap.inventory.dto.InventoryListSearchDTO(
					iv.id, iv.inventoryCode
				)
			FROM Inventory iv
			WHERE LOWER(iv.name) LIKE CONCAT('%',LOWER(?1),'%')
			""")
	public List<InventoryListSearchDTO> searchByName(String name);
	
	
	@Query("""
			SELECT iv FROM Inventory iv WHERE iv.inventoryCode = ?1
			""")
	public Inventory findByInventoryCode(String inventoryCode);
	
}
