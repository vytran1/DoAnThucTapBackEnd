package com.thuctap.reports.dto;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ReportAggregator {
	
	@JsonProperty("total_values")
	private BigDecimal totalValues;
	private List<ReportItemDTO> items;
	
	
	
	
	public ReportAggregator(BigDecimal totalValues, List<ReportItemDTO> items) {
		super();
		this.totalValues = totalValues;
		this.items = items;
	}


	public ReportAggregator() {
		super();
		// TODO Auto-generated constructor stub
	}


	public BigDecimal getTotalValues() {
		return totalValues;
	}


	public void setTotalValues(BigDecimal totalValues) {
		this.totalValues = totalValues;
	}


	public List<ReportItemDTO> getItems() {
		return items;
	}


	public void setItems(List<ReportItemDTO> items) {
		this.items = items;
	}
	
	
	
}
