package com.thuctap.inventory_order;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thuctap.common.exceptions.OrderNotFoundException;
import com.thuctap.common.exceptions.ProductNotFoundException;
import com.thuctap.common.exceptions.ProductVariantSkuNotFoundException;
import com.thuctap.common.exceptions.StatusNotFoundException;
import com.thuctap.inventory_order.dto.InventoryOrderOverviewDTO;
import com.thuctap.inventory_order.dto.InventoryOrderSavingRequestAggregatorDTO;
import com.thuctap.inventory_order.dto.InventoryOrderStatusDTO;

@RestController
@RequestMapping("/api/orders")
public class InventoryOrderController {

	@Autowired
	private InventoryOrderService service;
	
	
	
	
	@PostMapping("")
	public ResponseEntity<?> saveOrder(@RequestBody InventoryOrderSavingRequestAggregatorDTO requestDTO){
		
			try {
				System.out.println(requestDTO.toString());
				service.saveOrder(requestDTO);
				return ResponseEntity.status(HttpStatus.CREATED).build();
			} catch (ProductVariantSkuNotFoundException e) {
				
				e.printStackTrace();
				Map<String,String> map = new HashMap<>();
				map.put("error",e.getMessage());
				map.put("sku",e.getSku());
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(map);
			} catch (StatusNotFoundException e) {
				e.printStackTrace();
				return ResponseEntity.internalServerError().build();
			}
	}
	
	
	@GetMapping("/{id}/overview")
	public ResponseEntity<?> getOverview(@PathVariable("id") Integer id){
		
		try {
			InventoryOrderOverviewDTO result = service.getOverview(id);
			return ResponseEntity.ok(result);
		} catch (OrderNotFoundException e) {
			return ResponseEntity.notFound().build();
		}
		
	}
	
	@GetMapping("/{id}/status")
	public ResponseEntity<List<InventoryOrderStatusDTO>> getStatus(@PathVariable("id") Integer id){
		return ResponseEntity.ok(service.getStatus(id));
	}
	
	
}
