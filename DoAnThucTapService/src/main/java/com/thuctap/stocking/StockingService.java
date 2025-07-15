package com.thuctap.stocking;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thuctap.common.exceptions.VariantNotFoundException;
import com.thuctap.common.product_variant.ProductVariant;
import com.thuctap.product_variant.ProductVariantRepository;
import com.thuctap.stocking.dto.StockingProductSearchDTO;

@Service
public class StockingService {
	
	@Autowired
	private StockingRepository repository;
	
	@Autowired
	private ProductVariantRepository productVariantRepository;
	
	
	public List<StockingProductSearchDTO> getStockingOfProduct(String sku) throws VariantNotFoundException{
		
		checkExistingOfProductVariant(sku);
		
		return repository.findStockingOfProduct(sku);
	}
	
	private void checkExistingOfProductVariant(String sku) throws VariantNotFoundException {
		Optional<ProductVariant> variantOPT = productVariantRepository.findBySkuCode(sku);
		
		if(variantOPT.isEmpty()) {
			throw new VariantNotFoundException("Not Exist Product Variant With The Given Sku code");
		}
		
	}
	
	
}
