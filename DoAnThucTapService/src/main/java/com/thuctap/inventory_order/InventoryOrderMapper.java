package com.thuctap.inventory_order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

import com.thuctap.common.inventory.Inventory;
import com.thuctap.common.inventory_employees.InventoryEmployee;
import com.thuctap.common.inventory_order.InventoryOrder;
import com.thuctap.common.inventory_order.InventoryOrderDetail;
import com.thuctap.common.inventory_order.InventoryOrderStatus;
import com.thuctap.common.supplier.Supplier;
import com.thuctap.inventory_order.dto.InventoryOrderDetailWithExpectedPriceDTO;
import com.thuctap.inventory_order.dto.InventoryOrderPageDTO;
import com.thuctap.inventory_order.dto.InventoryOrderSavingRequestAggregatorDTO;
import com.thuctap.inventory_order.dto.InventoryOrderStatusDTO;
import com.thuctap.utility.UtilityGlobal;

public class InventoryOrderMapper {

	
	public static InventoryOrder toOrder(InventoryOrderSavingRequestAggregatorDTO requestDTO) {
		InventoryOrder inventoryOrder = new InventoryOrder();
		inventoryOrder.setOrderCode("ORDER-" + LocalDateTime.now().toString());
		inventoryOrder.setSupplier(new Supplier(requestDTO.getSupplier()));
		inventoryOrder.setEmployee(new InventoryEmployee(UtilityGlobal.getIdOfCurrentLoggedUser()));
		return inventoryOrder;
	}
	
	
	public static InventoryOrderDetail toDetail(InventoryOrderDetailWithExpectedPriceDTO requestDTO, Integer orderId) {
		
		InventoryOrderDetail detail = new InventoryOrderDetail();
		detail.setQuantity(requestDTO.getQuantity());
		detail.setOrder(new InventoryOrder(orderId));
		detail.setExpectedPrice(requestDTO.getExpectedPrice());
		
		return detail;
		
	}
	
	public static InventoryOrderPageDTO toPageDTO(InventoryOrder order) {
		
		InventoryOrderPageDTO pageDTO = new InventoryOrderPageDTO();
		pageDTO.setEmployee(order.getEmployee().getFullName());
		pageDTO.setOrderCode(order.getOrderCode());
		pageDTO.setStockInDate(order.getCompletedAt());
		return pageDTO;
	};
	
	
	
	public static InventoryOrderStatusDTO toStatus(InventoryOrderStatus entity) {
		
		InventoryOrderStatusDTO result = new InventoryOrderStatusDTO();
		result.setCreatedAt(entity.getCreatedAt().toString());
		result.setDescription(entity.getStatus().getDescription());
		result.setStatus(entity.getStatus().getName());
		
		if(!Objects.equals(entity.getEmployee(),null)) {
			result.setUpdater(entity.getEmployee().getFullName());
		}
		
		if(!Objects.equals(entity.getSupplier(),null)) {
			result.setUpdater(entity.getSupplier().getFullContact());
		}
		
		
		return result;
		
	}
	
}
