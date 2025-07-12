package com.thuctap.product_variant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thuctap.product_variant.dto.CheckExistOfSkuCodeResponse;

@Service
public class ProductVariantService {
	
	@Autowired
	private ProductVariantRepository productVariantRepository;
	
	
	public CheckExistOfSkuCodeResponse checkExistOfSkuCode(String sku) {
		
		CheckExistOfSkuCodeResponse response = new CheckExistOfSkuCodeResponse();
		
		response.setDupplicate(productVariantRepository.existsByBaseSku(sku));
		
		return response;
		
	}
	
}
