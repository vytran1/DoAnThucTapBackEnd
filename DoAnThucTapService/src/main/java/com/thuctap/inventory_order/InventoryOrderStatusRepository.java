package com.thuctap.inventory_order;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.thuctap.common.inventory_order.InventoryOrderStatus;
import com.thuctap.common.inventory_order.InventoryOrderStatusId;

public interface InventoryOrderStatusRepository extends JpaRepository<InventoryOrderStatus,InventoryOrderStatusId> {

	
	
	
	@Query(value = """
			SELECT t.name 
			FROM inventory_order_status iot 
			JOIN importing_status t ON iot.status_id = t.id 
            WHERE iot.order_id = ?1 
            ORDER BY iot.created_at DESC 
            LIMIT 1
			""",nativeQuery = true)
	public String getCurrentStatusByOrderId(Integer orderId);
	
	
	@Query("""
			 SELECT ios 
			 FROM InventoryOrderStatus ios 
			 WHERE ios.order.id = ?1
			 ORDER BY ios.createdAt ASC
			""")
	public List<InventoryOrderStatus> getAllStatusBelongToOneOrder(Integer id);
	
	@Query("""
		    SELECT COUNT(ios) > 0
		    FROM InventoryOrderStatus ios
		    WHERE ios.order.id = :orderId
		      AND ios.supplier.id = :supplierId
		      AND ios.status.name IN ('REVIEWED_DECIDED_PRICE', 'REVIEWED_REJECT')
		""")
	boolean hasSupplierAlreadyResponded(
		    @Param("orderId") Integer orderId,
		    @Param("supplierId") Integer supplierId
		);
	
	
	
}
