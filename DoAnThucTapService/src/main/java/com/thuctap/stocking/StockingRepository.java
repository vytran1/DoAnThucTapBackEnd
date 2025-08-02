package com.thuctap.stocking;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.thuctap.common.stocking.Stocking;
import com.thuctap.common.stocking.StockingId;
import com.thuctap.stocking.dto.StockingInventorySearchDTO;
import com.thuctap.stocking.dto.StockingProductSearchDTO;
import com.thuctap.stocking.dto.StockingReportDTO;
import com.thuctap.stocking.dto.StockingSummaryDTO;

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

	@Query("""
			SELECT
				new com.thuctap.stocking.dto.StockingSummaryDTO(
						p.sku,
						p.nameOverride,
						SUM(s.quantity)
					)
			FROM Stocking s
			JOIN s.productVariant p
			GROUP BY p.sku, p.nameOverride
			""")
	public List<StockingSummaryDTO> getTotalStockingGroupBySku();

	@Query("""
			    SELECT new com.thuctap.stocking.dto.StockingReportDTO(
			        pv.nameOverride,
			        pv.sku,
			        COALESCE(SUM(CASE WHEN s.inventory.id = :inventoryId THEN s.quantity ELSE 0 END), 0),
			        pv.price * COALESCE(SUM(CASE WHEN s.inventory.id = :inventoryId THEN s.quantity ELSE 0 END), 0)
			    )
			    FROM ProductVariant pv
			    LEFT JOIN Stocking s ON s.productVariant.sku = pv.sku
			    WHERE pv.isDelete = false
			    GROUP BY pv.nameOverride, pv.sku, pv.price
			    ORDER BY
			        CASE
			            WHEN COALESCE(SUM(CASE WHEN s.inventory.id = :inventoryId THEN s.quantity ELSE 0 END), 0) > 0 THEN 0
			            ELSE 1
			        END,
			        COALESCE(SUM(CASE WHEN s.inventory.id = :inventoryId THEN s.quantity ELSE 0 END), 0) DESC
			""")
	Page<StockingReportDTO> getStockingReportOfInventory(@Param("inventoryId") Integer inventoryId, Pageable pageable);

	@Query("""
			    SELECT new com.thuctap.stocking.dto.StockingReportDTO(
			        pv.nameOverride,
			        pv.sku,
			        COALESCE(SUM(s.quantity), 0),
			        pv.price * COALESCE(SUM(s.quantity), 0)
			    )
			    FROM ProductVariant pv
			    LEFT JOIN Stocking s ON s.productVariant.sku = pv.sku
			    WHERE pv.isDelete = false
			    GROUP BY pv.nameOverride, pv.sku, pv.price
			    ORDER BY
			        CASE
			            WHEN COALESCE(SUM(s.quantity), 0) > 0 THEN 0
			            ELSE 1
			        END,
			        COALESCE(SUM(s.quantity), 0) DESC
			""")
	Page<StockingReportDTO> getStockingReportAllWarehouses(Pageable pageable);
	
	@Query("""
		    SELECT SUM(pv.price * s.quantity)
		    FROM ProductVariant pv
		    JOIN Stocking s ON s.productVariant.sku = pv.sku
		    WHERE s.inventory.id = :inventoryId AND pv.isDelete = false
		""")
	Long getTotalStockValueOfInventory(@Param("inventoryId") Integer inventoryId);
	
	@Query("""
		    SELECT SUM(pv.price * s.quantity)
		    FROM ProductVariant pv
		    JOIN Stocking s ON s.productVariant.sku = pv.sku
		    WHERE pv.isDelete = false
		""")
		Long getTotalStockValueOfAllWarehouses();
	
}
