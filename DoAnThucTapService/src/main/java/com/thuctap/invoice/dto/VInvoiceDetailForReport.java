package com.thuctap.invoice.dto;

import java.math.BigDecimal;

public class VInvoiceDetailForReport {
	private String productName;
	private BigDecimal unitPrice;
	private Integer quantity;
	private BigDecimal total;
	
	
	
	
	public VInvoiceDetailForReport() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public BigDecimal getUnitPrice() {
		return unitPrice;
	}
	public void setUnitPrice(BigDecimal unitPrice) {
		this.unitPrice = unitPrice;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public BigDecimal getTotal() {
		return total;
	}
	public void setTotal(BigDecimal total) {
		this.total = total;
	}
	
	
	
}
