package com.thuctap.inventory;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thuctap.common.inventory.Inventory;

@Service
public class InventoryService {
	@Autowired
	private InventoryRepository inventoryRepository;
	
	public InventoryDTO convertToDTO(Inventory inventory) {
		InventoryDTO dto = new InventoryDTO();
		dto.setId(inventory.getId());
		dto.setInventoryCode(inventory.getInventoryCode());
		dto.setInventoryName(inventory.getName());
		dto.setAddress(inventory.getAddress());
		dto.setDistrictID(inventory.getDistrict().getId());
		dto.setDistrictName(inventory.getDistrict().getName());
		
		return dto;
	}
	
	public List<InventoryDTO> getAllInventories() { 
		List<Inventory> inventories = inventoryRepository.findAll();
		
		return inventories.stream().map(this::convertToDTO)
				.collect(Collectors.toList());
	}
	
}
