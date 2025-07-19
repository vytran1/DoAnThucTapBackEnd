package com.thuctap.product_variant;

import com.thuctap.common.product_variant.ProductVariant;
import com.thuctap.product_variant.dto.ProductVariantForTransactionDTO;

public class ProductVariantMapper {
	
	
	
	public static ProductVariantForTransactionDTO toVariantForTransaction(ProductVariant variant) {
		
		ProductVariantForTransactionDTO result = new ProductVariantForTransactionDTO(variant.getSku(),variant.getNameOverride());
		
		return result;
		
	}
	
}
