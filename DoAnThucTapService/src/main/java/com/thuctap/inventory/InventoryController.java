package com.thuctap.inventory;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/v1/inventories")
@CrossOrigin(
	    origins = "http://localhost:4200",
	    methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS},
	    allowedHeaders = "*",
	    allowCredentials = "true",
	    maxAge = 3600
	)
public class InventoryController {

    @Autowired
    private InventoryService inventoryService; 


    @GetMapping
    public ResponseEntity<Page<InventoryDTO>> getAllInventories(
            @RequestParam(required = false) String keyword, 
            Pageable pageable) {
        
        Page<InventoryDTO> inventoriesPage = inventoryService.findAllOrSearch(keyword, pageable);
        return ResponseEntity.ok(inventoriesPage);
    }

  
    @GetMapping("/{id}")
    public ResponseEntity<InventoryDTO> getInventoryById(@PathVariable("id") Integer id) {
        InventoryDTO inventory = inventoryService.getInventoryById(id);
        return ResponseEntity.ok(inventory);
    }


    @PostMapping
    public ResponseEntity<InventoryDTO> createInventory(@Valid @RequestBody InventoryDTO inventoryDTO) {
        InventoryDTO createdInventory = inventoryService.createInventory(inventoryDTO);
        return new ResponseEntity<>(createdInventory, HttpStatus.CREATED);
    }


    @PutMapping("/{id}")
    public ResponseEntity<InventoryDTO> updateInventory(@PathVariable("id") Integer id, @RequestBody InventoryDTO inventoryDTO) {
        InventoryDTO updatedInventory = inventoryService.updateInventory(id, inventoryDTO);
        return ResponseEntity.ok(updatedInventory);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInventory(@PathVariable("id") Integer id) {
        inventoryService.deleteInventory(id);
        return ResponseEntity.noContent().build();
    }
}

