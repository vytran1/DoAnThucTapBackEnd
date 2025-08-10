package com.thuctap.exporting_form.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;


public class QuotePriceInformationAggregator {
	@JsonProperty("total_quantity")
	private Long totalQuantity;
	
	@JsonProperty("quote_price_date")
	private QuotePriceDataDTO quotePriceData;
	
	
	@JsonProperty("total_value_without_discount")
	private BigDecimal totalValueWithoutDiscount;
	@JsonProperty("total_value_with_discount")
	private BigDecimal totalValueWithDiscount;
	
	public QuotePriceInformationAggregator() {
		
	}
	
	
	public Long getTotalQuantity() {
		return totalQuantity;
	}
	public void setTotalQuantity(Long totalQuantity) {
		this.totalQuantity = totalQuantity;
	}
	
	


	public QuotePriceDataDTO getQuotePriceData() {
		return quotePriceData;
	}


	public void setQuotePriceData(QuotePriceDataDTO quotePriceData) {
		this.quotePriceData = quotePriceData;
	}


	public BigDecimal getTotalValueWithoutDiscount() {
		return totalValueWithoutDiscount;
	}


	public void setTotalValueWithoutDiscount(BigDecimal totalValueWithoutDiscount) {
		this.totalValueWithoutDiscount = totalValueWithoutDiscount;
	}


	public BigDecimal getTotalValueWithDiscount() {
		return totalValueWithDiscount;
	}


	public void setTotalValueWithDiscount(BigDecimal totalValueWithDiscount) {
		this.totalValueWithDiscount = totalValueWithDiscount;
	}
	
	
	
	
	
}
