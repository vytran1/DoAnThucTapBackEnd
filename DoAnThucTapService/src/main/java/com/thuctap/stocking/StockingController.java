package com.thuctap.stocking;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thuctap.common.exceptions.VariantNotFoundException;
import com.thuctap.stocking.dto.StockingInventorySearchDTO;
import com.thuctap.stocking.dto.StockingProductSearchDTO;

@RestController
@RequestMapping("/api/stocking")
public class StockingController {

	@Autowired
	private StockingService stockingService;
	
	@GetMapping("/product/{sku}")
	public ResponseEntity<?> getStockingOfProduct(@PathVariable("sku") String sku){
		
		
		try {
			List<StockingProductSearchDTO> result = stockingService.getStockingOfProduct(sku);
			return ResponseEntity.ok(result);
		} catch (VariantNotFoundException e) {
			// TODO Auto-generated catch block
			return ResponseEntity.notFound().build();
		}
		
	}
	
	
	@GetMapping("/inventory/{id}")
	public ResponseEntity<List<StockingInventorySearchDTO>> getStockingOfInventory(@PathVariable("id") Integer id){
		
		List<StockingInventorySearchDTO> result = stockingService.getStockingOfInventory(id);
		
		return ResponseEntity.ok(result);
	}
	
}
