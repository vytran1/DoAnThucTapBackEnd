package com.thuctap.product_variant;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thuctap.audit.AuditRepository;
import com.thuctap.common.audit.Audit;
import com.thuctap.common.exceptions.VariantNotFoundException;
import com.thuctap.common.inventory_employees.InventoryEmployee;
import com.thuctap.common.product_variant.ProductVariant;
import com.thuctap.product_variant.dto.CheckExistOfSkuCodeResponse;
import com.thuctap.product_variant.dto.ProductVariantDTO;
import com.thuctap.product_variant.dto.ProductVariantDetailDTO;
import com.thuctap.utility.UtilityGlobal;

@Service
public class ProductVariantService {
	
	@Autowired
	private ProductVariantRepository productVariantRepository;
	
	@Autowired
	private AuditRepository auditRepository;
	
	
	public CheckExistOfSkuCodeResponse checkExistOfSkuCode(String sku) {
		
		CheckExistOfSkuCodeResponse response = new CheckExistOfSkuCodeResponse();
		
		response.setDupplicate(productVariantRepository.existsByBaseSku(sku));
		
		return response;
	}
	
	
	
	public ProductVariantDetailDTO updateVariant(ProductVariantDetailDTO newDTO) throws VariantNotFoundException {
		
		Optional<ProductVariant> variantOPT = productVariantRepository.findBySkuCode(newDTO.getSku());
		
		if(variantOPT.isEmpty()) {
			throw new VariantNotFoundException("Variant Not Exist");
		}
		
		ProductVariant oldVariant = variantOPT.get();
		
		String oldName = oldVariant.getNameOverride();
		BigDecimal oldPrice = oldVariant.getPrice();
		
		
		oldVariant.setNameOverride(newDTO.getName());
		oldVariant.setPrice(newDTO.getPrice());
		
		productVariantRepository.save(oldVariant);
		
		
		saveChanges(newDTO,oldName,oldPrice,"UPDATE");   
		
		
		
		return newDTO;
		
	}



	private void saveChanges(ProductVariantDetailDTO newDTO, String oldName, BigDecimal oldPrice, String action) {
		// TODO Auto-generated method stub
		if(!newDTO.getName().equals(oldName)) {
			Audit auditForName = buildAuditRecord("ProductVariant",newDTO.getSku(),oldName,newDTO.getName(),action,"nameOverride");
			auditRepository.save(auditForName);
		}
		
		if(!newDTO.getPrice().equals(oldPrice)) {
			Audit auditForPrice = buildAuditRecord("ProductVariant",newDTO.getSku(),oldPrice.toString(),newDTO.getPrice().toString(),action,"price");
			auditRepository.save(auditForPrice);
		}
		
	}
	
	private Audit buildAuditRecord(String tableName, String recordId, String oldName, String newName, String action, String fieldName) {
		
		Audit audit = new Audit();
		audit.setAction(action);
		audit.setFieldName(fieldName);
		audit.setInventoryEmployee(new InventoryEmployee(UtilityGlobal.getIdOfCurrentLoggedUser()));
		audit.setIsDelete(false);
		audit.setNewValue(newName);
		audit.setOldValue(oldName);
		audit.setTableName(tableName);
		audit.setRecordId(recordId);
		
		return audit;
		
		
	}
	
	
	
	
	
}
