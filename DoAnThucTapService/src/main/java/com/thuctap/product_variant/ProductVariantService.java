package com.thuctap.product_variant;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.thuctap.audit.AuditRepository;
import com.thuctap.common.audit.Audit;
import com.thuctap.common.exceptions.VariantNotFoundException;
import com.thuctap.common.inventory.Inventory;
import com.thuctap.common.inventory_employees.InventoryEmployee;
import com.thuctap.common.product_variant.ProductVariant;
import com.thuctap.inventory.vytran.VInventoryRepository;
import com.thuctap.product.ProductSpecification;
import com.thuctap.product_variant.dto.CheckExistOfSkuCodeResponse;
import com.thuctap.product_variant.dto.ProductVariantDTO;
import com.thuctap.product_variant.dto.ProductVariantDetailDTO;
import com.thuctap.product_variant.dto.ProductVariantForTransactionAggregator;
import com.thuctap.product_variant.dto.ProductVariantForTransactionDTO;
import com.thuctap.product_variant.dto.ProductVariantWithStockAggregator;
import com.thuctap.product_variant.dto.ProductVariantWithStockDTO;
import com.thuctap.utility.PageDTO;
import com.thuctap.utility.UtilityGlobal;

@Service
public class ProductVariantService {
	
	@Autowired
	private ProductVariantRepository productVariantRepository;
	
	@Autowired
	private AuditRepository auditRepository;
	
	@Autowired
	private VInventoryRepository vInventoryRepository;
	
	
	public CheckExistOfSkuCodeResponse checkExistOfSkuCode(String sku) {
		
		CheckExistOfSkuCodeResponse response = new CheckExistOfSkuCodeResponse();
		
		response.setDupplicate(productVariantRepository.existsByBaseSku(sku));
		
		return response;
	}
	
	
	
	
	public ProductVariantForTransactionAggregator findAllVariantForTransactions(int pageNum, 
			int pageSize, 
			String sortField, 
			String sortDir, 
			String name,
			Integer categoryId){
		
		Pageable pageable = setupPageRequestObject(pageNum, pageSize, sortField, sortDir);
		
		Specification<ProductVariant> specification = ProductSpecification.hasName(name).and(ProductSpecification.hasCategory(categoryId));
		
		Page<ProductVariant> pages = productVariantRepository.findAll(specification,pageable);
		
		ProductVariantForTransactionAggregator result = buildResult(pages,sortField,sortDir);
		
		return result;
		
	}
	
	
	public ProductVariantWithStockAggregator findAllVariantWithStock(
			int pageNum, 
			int pageSize, 
			String sortField, 
			String sortDir, 
			String name,
			Integer categoryId
			) {
		
		String inventoryCode = UtilityGlobal.getInventoryCodeOfCurrentLoggedUser();
		
		Inventory inventory = vInventoryRepository.findByInventoryCode(inventoryCode);
		
		Pageable peagPageable = UtilityGlobal.setUpPageRequest(pageNum,pageSize, sortField, sortDir);
		
		Page<ProductVariantWithStockDTO> page = productVariantRepository.findForSaleOfPoint(inventory.getId(), name, categoryId, peagPageable);
		
		ProductVariantWithStockAggregator result = new ProductVariantWithStockAggregator();
		
		result.setVariants(page.getContent());
		result.setPage(UtilityGlobal.buildPageDTO(sortField, sortDir, page));
		
		return result;
		
		
	}
	
	
	
	
	
	
	
	
	
	private ProductVariantForTransactionAggregator buildResult(Page<ProductVariant> pages, String sortField, String sortDir) {
		
		PageDTO page = UtilityGlobal.buildPageDTO(sortField, sortDir, pages);
		
		
		List<ProductVariantForTransactionDTO> variants = pages.getContent().stream().map(ProductVariantMapper::toVariantForTransaction).toList();
		
		
		ProductVariantForTransactionAggregator result = new ProductVariantForTransactionAggregator();
		
		result.setPage(page);
		result.setVariants(variants);
		
		return result;
		
	}
	
	
	public Pageable setupPageRequestObject(int pageNum, int pageSize, String sortField, String sortDir) {
		Sort sort = Sort.by(sortField);
		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
		
		Pageable pageable = PageRequest.of(pageNum - 1, pageSize, sort);
		return pageable;
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
