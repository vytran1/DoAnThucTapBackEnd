package com.thuctap.inventory;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.thuctap.common.district.District;
import com.thuctap.common.exceptions.CannotDeleteException;
import com.thuctap.common.exceptions.InventoryAlreadyExistException;
import com.thuctap.common.inventory.Inventory;
import com.thuctap.district.DistrictRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InventoryService {

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private DistrictRepository districtRepository;


    public Page<InventoryDTO> findAllOrSearch(String keyword, Pageable pageable) {
        Page<Inventory> inventoryPage;

        // Nếu keyword không rỗng và không null, thực hiện tìm kiếm
        if (keyword != null && !keyword.trim().isEmpty()) {
            inventoryPage = inventoryRepository.searchByKeyword(keyword, pageable);
        } else {
            // Ngược lại, lấy tất cả (đã phân trang)
            inventoryPage = inventoryRepository.findAllNotDeleted(pageable);
        }
        
        return inventoryPage.map(this::convertToDto);
    }

    public InventoryDTO getInventoryById(Integer id) {
        Inventory inventory = inventoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory", "id", id));
        return convertToDto(inventory);
    }

    @Transactional 
    public InventoryDTO createInventory(InventoryDTO inventoryDTO) {
    	
    	Optional<Inventory> existingInventory = inventoryRepository.findByInventoryCode(inventoryDTO.getInventoryCode());
    	
    	if(existingInventory.isPresent()) {
    		throw new InventoryAlreadyExistException("Inventory with code '" + inventoryDTO.getInventoryCode() + "' already exists. ");
    	}
    	
        District district = districtRepository.findById(inventoryDTO.getDistrictID())
                .orElseThrow(() -> new ResourceNotFoundException("District", "id", inventoryDTO.getDistrictID()));

        Inventory inventory = new Inventory();
        inventory.setInventoryCode(inventoryDTO.getInventoryCode());
        inventory.setName(inventoryDTO.getInventoryName());
        inventory.setAddress(inventoryDTO.getAddress());
        inventory.setDistrict(district);
        inventory.setIsDelete(false); 

        Inventory savedInventory = inventoryRepository.save(inventory);

        return convertToDto(savedInventory);
    }

    @Transactional
    public InventoryDTO updateInventory(Integer id, InventoryDTO inventoryDTO) {
        Inventory existingInventory = inventoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory", "id", id));
        
        if(!existingInventory.getInventoryCode().equals(inventoryDTO.getInventoryCode())) { 
        	inventoryRepository.findByInventoryCodeAndIdNot(inventoryDTO.getInventoryCode(), id)
        	.ifPresent(item -> { 
        		throw new InventoryAlreadyExistException("Inventory with code '" + inventoryDTO.getInventoryCode() + "' already exists. " );
        	});
        }
        
        
        if(inventoryDTO.getInventoryCode() != null) { 
        	existingInventory.setInventoryCode(inventoryDTO.getInventoryCode());
        }
        
        if(inventoryDTO.getInventoryName() != null) { 
        	existingInventory.setName(inventoryDTO.getInventoryName());
        }
        
        if(inventoryDTO.getAddress() != null)  {
        	existingInventory.setAddress(inventoryDTO.getAddress());
        }
        
        if (inventoryDTO.getDistrictID() != null) {
            District district = districtRepository.findById(inventoryDTO.getDistrictID())
                    .orElseThrow(() -> new ResourceNotFoundException("District", "id", inventoryDTO.getDistrictID()));
            existingInventory.setDistrict(district);
        }

        Inventory updatedInventory = inventoryRepository.save(existingInventory);

        return convertToDto(updatedInventory);
    }

    @Transactional
    public void deleteInventory(Integer id) {
        // findById sẽ hoạt động vì tại thời điểm này, is_delete vẫn là false
        Inventory inventory = inventoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory", "id", id));
        
        // Đánh dấu là đã xóa
        inventory.setIsDelete(true); 
        
        // Lưu lại thay đổi vào cơ sở dữ liệu
        inventoryRepository.save(inventory);
    }


    private InventoryDTO convertToDto(Inventory inventory) {
        InventoryDTO dto = new InventoryDTO();
        dto.setId(inventory.getId());
        dto.setInventoryCode(inventory.getInventoryCode());
        dto.setInventoryName(inventory.getName());
        dto.setAddress(inventory.getAddress());
        
        if (inventory.getDistrict() != null) {
            dto.setDistrictID(inventory.getDistrict().getId());
            dto.setDistrictName(inventory.getDistrict().getName());
        }
        
        return dto;
    }
}
