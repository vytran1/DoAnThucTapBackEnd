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
	private Long averageSaledQuantity;
	@JsonProperty("need_to_be_imported")
	private boolean needToBeImported;
	
	@JsonProperty("start_date")
	private LocalDateTime startDate;
	@JsonProperty("end_date")
	private LocalDateTime endDate;
	
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
	public Long getAverageSaledQuantity() {
		return averageSaledQuantity;
	}
	public void setAverageSaledQuantity(Long averageSaledQuantity) {
		this.averageSaledQuantity = averageSaledQuantity;
	}
	public boolean isNeedToBeImported() {
		return needToBeImported;
	}
	public void setNeedToBeImported(boolean needToBeImported) {
		this.needToBeImported = needToBeImported;
	}
	
	
	
}
