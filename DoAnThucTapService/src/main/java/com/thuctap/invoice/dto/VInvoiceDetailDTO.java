package com.thuctap.invoice.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

public class VInvoiceDetailDTO {
	private String sku;
	private String name;
	@JsonProperty("unit_price")
	private BigDecimal unitPrice;
	private Integer quantity;
	public VInvoiceDetailDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		this.sku = sku;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	
	
}
