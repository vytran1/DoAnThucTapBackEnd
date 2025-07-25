package com.thuctap.inventory_order;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.thuctap.common.inventory_order.InventoryOrderDetail;
import com.thuctap.common.inventory_order.InventoryOrderDetailId;
import com.thuctap.inventory_order.dto.InventoryOrderDetailForOverviewDTO;

public interface InventoryOrderDetailRepository extends JpaRepository<InventoryOrderDetail,InventoryOrderDetailId> {
		
	
	
	
	
	@Query("""
			SELECT new com.thuctap.inventory_order.dto.InventoryOrderDetailForOverviewDTO(
				p.nameOverride, 
				p.sku, 
				iod.quantity, 
				COALESCE(iod.costPrice, 0),
			    COALESCE(iod.quoteCostPrice, 0),
			    COALESCE(iod.expectedPrice, 0) 
			)
			FROM InventoryOrderDetail iod
			JOIN iod.productVariant p
			WHERE iod.order.id = ?1
			""")
	public List<InventoryOrderDetailForOverviewDTO> findAllDetailsBelongToAOrderForOverview(Integer id);
	
	
	
	@Query("""
			SELECT iod FROM InventoryOrderDetail iod WHERE iod.order.id = ?1
			""")
	public List<InventoryOrderDetail> findAllDetailsBelongToAOrder(Integer orderId);
	
	
	
	@Query("""
			SELECT SUM(iod.quantity) 
			FROM InventoryOrderDetail iod
			WHERE iod.order.id = ?1
			""")
	public Long sumOfQuantity(Integer orderId);

}
