package com.thuctap.stocking;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.thuctap.common.stocking.Stocking;
import com.thuctap.common.stocking.StockingId;
import com.thuctap.stocking.dto.StockingInventorySearchDTO;
import com.thuctap.stocking.dto.StockingProductSearchDTO;

public interface StockingRepository extends JpaRepository<Stocking,StockingId> {
	
	
	
	
	@Query("""
			SELECT new com.thuctap.stocking.dto.StockingProductSearchDTO(
				iv.inventoryCode,
				COALESCE(SUM(s.quantity), 0)
			)
			FROM Stocking s 
			LEFT JOIN s.inventory iv
			WHERE s.productVariant.sku = ?1
			GROUP BY iv.inventoryCode
			""")
	public List<StockingProductSearchDTO> findStockingOfProduct(String sku);
	
	
	
	
	@Query("""
			SELECT new com.thuctap.stocking.dto.StockingInventorySearchDTO(
				p.id, p.image, pv.sku, s.quantity
			) 
			FROM Stocking s
			INNER JOIN s.productVariant pv
			INNER JOIN pv.product p
			WHERE s.inventory.id = ?1
			""")
	public List<StockingInventorySearchDTO> findStockingOfInventory(Integer id);
	
	
	
	
	
}
