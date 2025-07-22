package com.thuctap.importing_form;

import java.time.LocalDateTime;

import com.thuctap.common.importing_form.ImportingForm;
import com.thuctap.common.importing_form.ImportingFormDetail;
import com.thuctap.common.importing_form.ImportingFormDetailId;
import com.thuctap.common.inventory_order.InventoryOrder;
import com.thuctap.common.inventory_order.InventoryOrderDetail;
import com.thuctap.common.product_variant.ProductVariant;

public class ImportingFormMapper {
	
	
	public static ImportingForm toImportingForm(InventoryOrder order) {
		
		ImportingForm importingForm = new ImportingForm();
		importingForm.setEmployee(order.getEmployee());
		importingForm.setImportingFormCode("IPF-" + LocalDateTime.now().toString());
		importingForm.setCreatedAt(LocalDateTime.now());
		importingForm.setCompletedAt(LocalDateTime.now());
		importingForm.setOrder(order);
		importingForm.setInventory(order.getInventory());
		importingForm.setSupplier(order.getSupplier());
		
		return importingForm;
		
	}
	
	
	public static ImportingFormDetail toImportingFormDetail(ImportingForm importingForm,InventoryOrderDetail orderDetail) {
		
		ProductVariant productVariant = orderDetail.getProductVariant();
		
		ImportingFormDetail importingFormDetail = new ImportingFormDetail();
		importingFormDetail.setId(new ImportingFormDetailId(importingForm.getId(),productVariant.getSku()));
		importingFormDetail.setCostPrice(orderDetail.getCostPrice());
		importingFormDetail.setProductVariant(productVariant);
		importingFormDetail.setQuantity(orderDetail.getQuantity());
		importingFormDetail.setImportingForm(importingForm);
		
		return importingFormDetail;
	}
	
}
