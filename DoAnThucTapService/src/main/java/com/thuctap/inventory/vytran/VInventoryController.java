package com.thuctap.inventory.vytran;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thuctap.inventory.dto.InventoryListSearchDTO;

@RestController
@RequestMapping("/api/inventory")
public class VInventoryController {

	@Autowired
	private VInventoryService inventoryService;
	
	
	
	@GetMapping("/search/name/{name}")
	public ResponseEntity<List<InventoryListSearchDTO>> searchByName(@PathVariable("name") String name){
		List<InventoryListSearchDTO> result = inventoryService.search(name);
		return ResponseEntity.ok(result);
	}
	
	
}
