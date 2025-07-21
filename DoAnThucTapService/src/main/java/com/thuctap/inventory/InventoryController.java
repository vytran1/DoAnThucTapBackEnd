package com.thuctap.inventory;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/inventories")
public class InventoryController {

	@Autowired
	private InventoryService inventoryService;
	
	@GetMapping
	public ResponseEntity<List<InventoryDTO>> getAllInventories() { 
		List<InventoryDTO> inventoryList = inventoryService.getAllInventories();
		return ResponseEntity.ok(inventoryList);
	}
}
