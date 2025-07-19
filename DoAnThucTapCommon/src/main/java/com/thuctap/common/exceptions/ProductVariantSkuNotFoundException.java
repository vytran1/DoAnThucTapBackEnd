package com.thuctap.common.exceptions;

public class ProductVariantSkuNotFoundException extends Exception{
	
	private final String sku;

    public ProductVariantSkuNotFoundException(String sku) {
        super("Product variant with SKU [" + sku + "] not found.");
        this.sku = sku;
    }

    public String getSku() {
        return sku;
    }
	
}
