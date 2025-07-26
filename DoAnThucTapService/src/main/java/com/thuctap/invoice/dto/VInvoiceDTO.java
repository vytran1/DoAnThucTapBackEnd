package com.thuctap.invoice.dto;

import java.math.BigDecimal;
import java.util.List;

public class VInvoiceDTO {
	private Integer quantity;
	private BigDecimal tax;
	private BigDecimal total;
	private List<VInvoiceDetailDTO> details;
	public VInvoiceDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public BigDecimal getTax() {
		return tax;
	}
	public void setTax(BigDecimal tax) {
		this.tax = tax;
	}
	public BigDecimal getTotal() {
		return total;
	}
	public void setTotal(BigDecimal total) {
		this.total = total;
	}
	public List<VInvoiceDetailDTO> getDetails() {
		return details;
	}
	public void setDetails(List<VInvoiceDetailDTO> details) {
		this.details = details;
	}
	
	
}
