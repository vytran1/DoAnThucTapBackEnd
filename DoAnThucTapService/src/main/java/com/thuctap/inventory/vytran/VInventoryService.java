package com.thuctap.inventory.vytran;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thuctap.inventory.dto.InventoryListSearchDTO;

@Service
public class VInventoryService {

	@Autowired
	private VInventoryRepository repository;
	
	
	public List<InventoryListSearchDTO> search(String name){
		return repository.searchByName(name);
	}
	
	
	public List<InventoryListSearchDTO> getInventories(){
		return repository.getListInventoryForDropDownList();
	}
	
}
