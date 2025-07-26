package com.thuctap.stocking.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StockingAnalystSuggestionAggregator {
	@JsonProperty("start_date")
	private LocalDateTime startDate;
	@JsonProperty("end_date")
	private LocalDateTime endDate;
	
	List<StockingAnalystSuggestion> suggestions;

	public StockingAnalystSuggestionAggregator() {
		super();
		// TODO Auto-generated constructor stub
	}

	public LocalDateTime getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDateTime startDate) {
		this.startDate = startDate;
	}

	public LocalDateTime getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDateTime endDate) {
		this.endDate = endDate;
	}

	public List<StockingAnalystSuggestion> getSuggestions() {
		return suggestions;
	}

	public void setSuggestions(List<StockingAnalystSuggestion> suggestions) {
		this.suggestions = suggestions;
	}
	
	
	
}
