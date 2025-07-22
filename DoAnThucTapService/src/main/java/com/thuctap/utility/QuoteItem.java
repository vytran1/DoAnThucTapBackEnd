package com.thuctap.utility;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

public class QuoteItem {
	private String sku;
	@JsonProperty("product_name")
    private String productName;
	@JsonProperty("quoted_price")
    private BigDecimal quotedPrice;
    private String currency;
    private String description;
    private Integer quantity;
	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		this.sku = sku;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public BigDecimal getQuotedPrice() {
		return quotedPrice;
	}
	public void setQuotedPrice(BigDecimal quotedPrice) {
		this.quotedPrice = quotedPrice;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	@Override
	public String toString() {
		return "QuoteItem [sku=" + sku + ", productName=" + productName + ", quotedPrice=" + quotedPrice + ", currency="
				+ currency + ", description=" + description + "]";
	}
    
    
}
