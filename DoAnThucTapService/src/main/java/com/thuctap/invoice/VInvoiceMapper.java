package com.thuctap.invoice;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.thuctap.common.inventory.Inventory;
import com.thuctap.common.inventory_employees.InventoryEmployee;
import com.thuctap.common.invoice.Invoice;
import com.thuctap.common.invoice.InvoiceDetail;
import com.thuctap.common.invoice.InvoiceDetailId;
import com.thuctap.common.product_variant.ProductVariant;
import com.thuctap.invoice.dto.VInvoiceDTO;
import com.thuctap.invoice.dto.VInvoiceDetailDTO;
import com.thuctap.invoice.dto.VInvoiceDetailForReport;

public class VInvoiceMapper {
	
	public static Invoice toInvoice(VInvoiceDTO dto, Inventory inventory, InventoryEmployee employee) {
		Invoice invoice = new Invoice();
		invoice.setCreatedAt(LocalDateTime.now());
		invoice.setEmployee(employee);
		invoice.setInventory(inventory);
		invoice.setTax(dto.getTax());
		invoice.setInvoiceCode("INVOICE-" + LocalDateTime.now().toString());
		return invoice;
	}
	
	
	public static InvoiceDetail toInvoiceDetail(VInvoiceDetailDTO dto,Invoice invoice) {
		
		InvoiceDetail detail = new InvoiceDetail();
		detail.setId(new InvoiceDetailId(invoice.getId(),dto.getSku()));
		detail.setInvoice(invoice);
		detail.setQuantity(dto.getQuantity());
		detail.setUnitPrice(dto.getUnitPrice());
		//detail.setProductVariant(new ProductVariant(dto.getSku()));
		
		return detail;
		
	}
	
	public static VInvoiceDetailForReport toInvoiceDetailForReport(VInvoiceDetailDTO dto) {
		VInvoiceDetailForReport detail = new VInvoiceDetailForReport();
		detail.setProductName(dto.getName());
		detail.setQuantity(dto.getQuantity());
		detail.setUnitPrice(dto.getUnitPrice());
		detail.setTotal(dto.getUnitPrice().multiply(new BigDecimal(dto.getQuantity())));
		return detail;
	}
	
}
