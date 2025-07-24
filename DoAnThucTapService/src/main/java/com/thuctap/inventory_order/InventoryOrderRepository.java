package com.thuctap.inventory_order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.thuctap.common.inventory_order.InventoryOrder;
import com.thuctap.inventory_order.dto.InventoryOrderPageDTO;

public interface InventoryOrderRepository extends JpaRepository<InventoryOrder,Integer> {
	
	
	
	@Query("UPDATE InventoryOrder io SET io.quoteShippingFee = ?1 WHERE io.id = ?2")
	@Modifying
	public void updateShippingFee(BigDecimal shippingFee,Integer orderId);
	
	
	
	@Query("""
			SELECT new com.thuctap.inventory_order.dto.InventoryOrderPageDTO(
					io.id,
					io.orderCode,
					io.completedAt,
					SUM(d.quantity),
					CONCAT(e.firstName,' ',e.lastName)
				) 
			FROM InventoryOrder io 
			LEFT JOIN io.employee e
			LEFT JOIN InventoryOrderDetail d ON io.id = d.order.id
			WHERE (?1 IS NULL OR io.createdAt >= ?1) 
				AND (?2 IS NULL OR io.createdAt <= ?2)
			GROUP BY io.id, io.orderCode, io.createdAt, e.firstName, e.lastName
			""")
	public Page<InventoryOrderPageDTO> search(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
	
	
}
