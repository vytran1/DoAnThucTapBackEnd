package com.thuctap.stocking.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StockingAnalystSuggestion {
	
	private String sku;
	private String name;
	@JsonProperty("total_current_stocking")
	private Long totalCurrentStocking;
	@JsonProperty("total_saled_quantity")
	private Long totalSaledQuantity;
	@JsonProperty("average_saled_quantity")
	private Double averageSaledQuantity;
	@JsonProperty("need_to_be_imported")
	private boolean needToBeImported;
	@JsonProperty("predict_quantity_for_next_period")
	private Long predictQuantityForNextPeriod;
	
	
	
	
	public StockingAnalystSuggestion() {
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
	public Long getTotalCurrentStocking() {
		return totalCurrentStocking;
	}
	public void setTotalCurrentStocking(Long totalCurrentStocking) {
		this.totalCurrentStocking = totalCurrentStocking;
	}
	public Long getTotalSaledQuantity() {
		return totalSaledQuantity;
	}
	public void setTotalSaledQuantity(Long totalSaledQuantity) {
		this.totalSaledQuantity = totalSaledQuantity;
	}
	
	public Double getAverageSaledQuantity() {
		return averageSaledQuantity;
	}
	public void setAverageSaledQuantity(Double averageSaledQuantity) {
		this.averageSaledQuantity = averageSaledQuantity;
	}
	public boolean isNeedToBeImported() {
		return needToBeImported;
	}
	public void setNeedToBeImported(boolean needToBeImported) {
		this.needToBeImported = needToBeImported;
	}
	public Long getPredictQuantityForNextPeriod() {
		return predictQuantityForNextPeriod;
	}
	public void setPredictQuantityForNextPeriod(Long predictQuantityForNextPeriod) {
		this.predictQuantityForNextPeriod = predictQuantityForNextPeriod;
	}
	
	
	
}
