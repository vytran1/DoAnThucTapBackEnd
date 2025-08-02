package com.thuctap.stocking.dto;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.thuctap.utility.PageDTO;

public class StockingReportAggregator {
	
	private List<StockingReportDTO> stockings;
	@JsonProperty("page_dto")
	private PageDTO pageDTO;
	@JsonProperty("total_values")
	private BigDecimal totalValues;
	public StockingReportAggregator() {
		super();
		// TODO Auto-generated constructor stub
	}
	public List<StockingReportDTO> getStockings() {
		return stockings;
	}
	public void setStockings(List<StockingReportDTO> stockings) {
		this.stockings = stockings;
	}
	public PageDTO getPageDTO() {
		return pageDTO;
	}
	public void setPageDTO(PageDTO pageDTO) {
		this.pageDTO = pageDTO;
	}
	public BigDecimal getTotalValues() {
		return totalValues;
	}
	public void setTotalValues(BigDecimal totalValues) {
		this.totalValues = totalValues;
	}
	
	
	
}
